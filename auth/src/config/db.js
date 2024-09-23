// const dotenv = require('dotenv');
// const { Pool } = require('pg');

// const { getDbName } = require('../context/configContext');
// dotenv.config();

// const pool = new Pool({
//   user: process.env.DB_USER,
//   host: process.env.DB_HOST,
//   database: process.env.DB_NAME,
//   password: process.env.DB_PASS,
//   port: process.env.DB_PORT,
// });

// pool.connect((err) => {
//   if (err) {
//     console.error('Erro ao conectar ao banco de dados', err);
//   } else {
//     console.log('Conectado ao banco de dados PostgreSQL');
//   }
// });

// module.exports = pool;

const dotenv = require('dotenv');
const { Pool } = require('pg');
const { configDatabase } = require('../context/configContext');

dotenv.config();

let pool;

const createPool = async () => {
    if (!pool) {
        pool = new Pool({
            user: configDatabase.dbUser,
            host: configDatabase.dbHost,
            database: configDatabase.dbName,
            password: configDatabase.dbPassword,
            port: configDatabase.dbPort,
        });

        try {
            await pool.connect();
            console.log('Conectado ao banco de dados PostgreSQL');
        } catch (err) {
            console.error('Erro ao conectar ao banco de dados', err);
            throw err; // Propaga o erro
        }
    }
};

const getPool = () => {
    if (!pool) {
        throw new Error('Database pool not initialized. Please call createPool first.');
    }
    return pool;
};

module.exports = { createPool, getPool };