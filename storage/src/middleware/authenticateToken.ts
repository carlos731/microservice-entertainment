import jwt from 'jsonwebtoken';
import { Request, Response, NextFunction } from 'express';

const secretKey = process.env.SECRET_KEY_JWT || 'your-long-secret-key-that-has-32-or-more-characters';

/* o token deve trazer as permissions do usuário assim para validar
{
  "id": "77a8e209-b1ba-4b96-afae-bbb8964aa39e",
  "is_super": false,
  "roles": [
    "User",
    "Admin"
  ],
  "permissions": [
    "create:user",
    "delete_entertainment"
  ],
  "iat": 1726786830,
  "exp": 1726787730
}
*/

export const authenticateToken = (req: Request, res: Response, next: NextFunction) => {
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split(' ')[1]; // O token deve ser enviado como "Bearer <token>"

    if (!token) {
        return res.status(401).json({ error: 'Access denied.' });
    }

    jwt.verify(token, secretKey, (err: any, decodedToken: any) => {
        if (err) {
            return res.status(403).json({ error: 'Invalid or expired token.' });
        }

        // Armazena o token decodificado no req.tokenDecoded para uso posterior
        (req as any).tokenDecoded = decodedToken;
        next();
    });
};

// Middleware de autorização baseado nas permissões contidas no token
export const authorizePermissions = (...requiredPermissions: string[]) => {
    return (req: Request, res: Response, next: NextFunction) => {
        const decodedToken = (req as any).tokenDecoded;
        const userPermissions = decodedToken?.permissions || [];

        const hasPermission = requiredPermissions.some(permission => userPermissions.includes(permission));

        if (!hasPermission) {
            return res.status(403).json({ error: 'Permission denied.' });
        }

        next();
    };
};