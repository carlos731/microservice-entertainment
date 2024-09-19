const cors = require('cors');

const corsConfig = {
    origin: 'http://example.com',
    methods: ['GET', 'POST', 'PUT', 'DELETE'],
    allowedHeaders: ['Content-Type', 'Authorization'],
};

module.exports = corsConfig;