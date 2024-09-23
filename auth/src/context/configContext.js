let configCache = null;

const setConfig = (config) => {
    configCache = config;
};

const getConfig = () => {
    if (!configCache) {
        throw new Error('Config not loaded');
    }
    return configCache;
};

const getSecretJwt = () => {
    if (!configCache) {
        throw new Error('Config not loaded');
    }
    return configCache['config.jwt.secret'];
}

const getServerPort = () => {
    return getConfig()['config.server.port'];
}

const getDbName = () => {
    return getConfig()['config.db.name'];
}

const configDatabase = {
    get dbPort() {
        return this.getValue('config.db.port');
    },
    get dbName() {
        return this.getValue('config.db.name');
    },
    get dbUser() {
        return this.getValue('config.db.user');
    },
    get dbPassword() {
        return this.getValue('config.db.password');
    },
    get dbHost() {
        return this.getValue('config.db.host');
    },
    getValue(key) {
        const config = getConfig();
        if (!config[key]) {
            throw new Error(`Config key ${key} not found`);
        }
        return config[key];
    }
};

module.exports = {
    setConfig,
    getConfig,
    getSecretJwt,
    getServerPort,
    getDbName,
    configDatabase
};