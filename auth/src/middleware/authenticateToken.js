const jwt = require('jsonwebtoken');
const { getSecretJwt } = require('../context/configContext');

const secretKey = process.env.JWT_SECRET || 'your-long-secret-key-that-has-32-or-more-characters';

const authenticateToken = (req, res, next) => {
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split(' ')[1];

    if (!token) {
        return res.status(401).json({ error: 'Access denied.' });
    }

    jwt.verify(token, getSecretJwt(), (err, decodedToken) => {
        if (err) {
            return res.status(403).json({ error: 'Invalid or expired token.' });
        }

        req.tokenDecoded = decodedToken;
        next();
    });
};

const authorizePermissions = (...requiredPermissions) => {
    return (req, res, next) => {
        const decodedToken = req.tokenDecoded;
        const userPermissions = decodedToken?.permissions || [];

        const hasPermission = requiredPermissions.some(permission => userPermissions.includes(permission));

        if (!hasPermission) {
            return res.status(403).json({ error: 'Permission denied.' });
        }

        next();
    };
};

module.exports = { authenticateToken, authorizePermissions };
