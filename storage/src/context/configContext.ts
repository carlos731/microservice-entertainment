type Config = {
    [key: string]: any;
};

let configCache: Config | null = null;

const setConfig = (config: Config): void => {
    configCache = config;
};

const getConfig = (): Config => {
    if (!configCache) {
        throw new Error('Config not loaded');
    }
    return configCache;
};

const getSecretJwt = (): string => {
    if (!configCache) {
        throw new Error('Config not loaded');
    }
    return configCache['config.jwt.secret'];
};

const getServerPort = (): number => {
    return getConfig()['config.server.port'];
};

const getDbName = (): string => {
    return getConfig()['config.db.name'];
};

const configDatabase = {
    get dbPort(): number {
        return this.getValue('config.db.port');
    },
    get dbName(): string {
        return this.getValue('config.db.name');
    },
    get dbUser(): string {
        return this.getValue('config.db.user');
    },
    get dbPassword(): string {
        return this.getValue('config.db.password');
    },
    get dbHost(): string {
        return this.getValue('config.db.host');
    },
    getValue(key: string): any {
        const config = getConfig();
        if (!config[key]) {
            throw new Error(`Config key ${key} not found`);
        }
        return config[key];
    }
};

export {
    setConfig,
    getConfig,
    getSecretJwt,
    getServerPort,
    getDbName,
    configDatabase
};
