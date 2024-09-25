import axios from 'axios';
import { setConfig } from '../context/configContext';

const configServer = async () => {
    try {
        const configServerUrl = process.env.CONFIG_SERVER_URL;
        const configUser = process.env.CONFIG_USER;
        const configPassword = process.env.CONFIG_PASSWORD;
        
        if (!configServerUrl || !configUser || !configPassword) {
            throw new Error('Uma ou mais variáveis de ambiente não estão definidas.');
        }

        const response = await axios.get(configServerUrl, {
            auth: {
                username: configUser,
                password: configPassword
            }
        });

        if (!response.data || !response.data.propertySources) {
            throw new Error('No configuration data returned');
        }

        console.log('Conectado ao servidor de configuração.');
        setConfig(response.data.propertySources[0].source);
    } catch (error) {
        console.error('Falha ao se conectar no Servidor de configuração');
        process.exit(1);
    }
};

export default configServer;
