import { Request, Response, NextFunction } from 'express';

const apiKey = process.env.API_KEY;

const apiKeyAuth = (req: Request, res: Response, next: NextFunction) => {
    const key = req.headers['x-api-key'];

    if (!key || key !== apiKey) {
        return res.status(403).json({ message: 'Acesso negado: chave de API inválida.' });
    }

    next();
};

export default apiKeyAuth;