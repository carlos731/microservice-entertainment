const rateLimit = require('express-rate-limit');

// Limitação de taxa: 100 requisições por 15 minutos
const limiter = rateLimit({
    windowMs: 15 * 60 * 1000, // Limite de 15 minutos, windowMs: 1 * 60 * 1000, // 1 minuto
    max: 100, // Limite de 100 requisições
    handler: (req, res) => {
        res.status(429).json({
            message: "Muitas requisições, tente novamente mais tarde."
        });
    }
    // keyGenerator: (req, res) => {
    //     return req.ip; // Usa o IP do usuário como chave
    // }
});

module.exports = limiter;