import { Pool } from 'pg';
import dotenv from 'dotenv';

dotenv.config();

const pool = new Pool({
    host: process.env.DB_HOST,
    port: parseInt(process.env.DB_PORT || '5432', 10),
    user: process.env.DB_USER,
    password: process.env.DB_PASSWORD,
    database: process.env.DB_NAME,
});

export const connectToDatabase = async (): Promise<void> => {
    try {
        await pool.query('SELECT 1');
        console.log('Database connection successful');
    } catch (error) {
        console.error('Database connection failed', error);
        throw error;
    }
};

export default pool;