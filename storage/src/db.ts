// import { Pool } from 'pg';
// import dotenv from 'dotenv';

// dotenv.config();

// const pool = new Pool({
//     host: process.env.DB_HOST,
//     port: parseInt(process.env.DB_PORT || '5432', 10),
//     user: process.env.DB_USER,
//     password: process.env.DB_PASSWORD,
//     database: process.env.DB_NAME,
// });

// export const connectToDatabase = async (): Promise<void> => {
//     try {
//         await pool.query('SELECT 1');
//         console.log('Database connection successful');
//     } catch (error) {
//         console.error('Database connection failed', error);
//         throw error;
//     }
// };

// export default pool;
import { Pool } from 'pg';
import dotenv from 'dotenv';
import { configDatabase } from './context/configContext';

dotenv.config();

let pool: Pool | null = null;

export const createPool = async (): Promise<void> => {
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

export const getPool = (): Pool => {
    if (!pool) {
        throw new Error('Database pool not initialized. Please call createPool first.');
    }
    return pool;
};