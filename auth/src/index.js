const express = require('express');
const cors = require('cors');
const limiter = require('./config/limiter');
// const pool = require('./config/db'); // Agora irá usar as configurações do configContext
const axios = require('axios');
const configServer = require('./config/configServer');

const authRoutes = require('./routes/authRoutes');
const userRoutes = require('./routes/userRoutes');
const roleRoutes = require('./routes/roleRoutes');
const permissionRoutes = require('./routes/permissionRoutes');

const { getServerPort } = require('./context/configContext');
const { createPool } = require('./config/db');

const app = express();

// Função para iniciar o servidor
const startServer = async () => {
    try {
        // Carrega as configurações do servidor
        await configServer();

        await createPool();

        // Agora que as configurações estão carregadas, você pode usar o pool
        app.use(cors());
        app.use(express.urlencoded({ extended: true }));
        app.use(express.json());
        app.use(limiter);

        // Rotas
        app.use('/auth', authRoutes);
        app.use('/user', userRoutes);
        app.use('/role', roleRoutes);
        app.use('/permission', permissionRoutes);

        const port = getServerPort() || process.env.PORT || 8000;
        app.listen(port, () => {
            console.log(`Server running on port: ${port}`);
        });
    } catch (error) {
        console.error('Failed to start the server:', error);
        process.exit(1);
    }
};

startServer();

module.exports = app;
