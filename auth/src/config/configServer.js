const axios = require('axios');
const { setConfig } = require('../context/configContext');

const configServer = async () => {
    try {
        const response = await axios.get(process.env.CONFIG_SERVER_URL);
        if (!response.data) {
            throw new Error('No configuration data returned');
        }
        console.error('Conectado ao servidor de configuração.');
        setConfig(response.data.propertySources[0].source);
    } catch (error) {
        console.error('Servidor de configuração indisponível.');
        process.exit(1);
    }
};

module.exports = configServer;