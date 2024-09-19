import jwt from 'jsonwebtoken';
import { Request, Response, NextFunction } from 'express';

const secretKey = process.env.SECRET_KEY_JWT || 'minhachavesecretadetoken';

/* o token trazer as permissions do usuário assim para validar
{
  "sub": "1234567890",
  "name": "John Doe",
  "iat": 1516239022,
  "roles": ["admin", "uploader"],
  "permissions": ["upload_file", "delete_file"]
}
*/

/* Bearer token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJyb2xlcyI6WyJhZG1pbiIsInN1cGVydXNlciJdLCJwZXJtaXNzaW9ucyI6WyJ1cGxvYWRfZmlsZSIsImRlbGV0ZV9maWxlIiwidXBkYXRlX2ZpbGUiLCJnZXRfZmlsZSJdfQ.lRB_SBI2o0GD6Tn9g3Q3PAYUPNYYDlXYkFvieMlVcCI */

// Middleware de autenticação
export const authenticateToken = (req: Request, res: Response, next: NextFunction) => {
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split(' ')[1]; // O token deve ser enviado como "Bearer <token>"

    if (!token) {
        return res.status(401).json({ error: 'Access denied.' });
    }

    // Verifica se o token é válido
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
        // Recupera as permissões do token decodificado
        const decodedToken = (req as any).tokenDecoded;
        const userPermissions = decodedToken?.permissions || []; // Assume que as permissões estão no campo 'permissions'

        // Verifica se o usuário tem pelo menos uma das permissões necessárias
        const hasPermission = requiredPermissions.some(permission => userPermissions.includes(permission));

        if (!hasPermission) {
            return res.status(403).json({ error: 'Permission denied.' });
        }

        next();
    };
};