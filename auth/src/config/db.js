const dotenv = require('dotenv');
const { Pool } = require('pg');

// Carrega as variáveis de ambiente do arquivo .env
dotenv.config();

const pool = new Pool({
  user: process.env.DB_USER,
  host: process.env.DB_HOST,
  database: process.env.DB_NAME,
  password: process.env.DB_PASS,
  port: process.env.DB_PORT,
});

pool.connect((err) => {
  if (err) {
    console.error('Erro ao conectar ao banco de dados', err);
  } else {
    console.log('Conectado ao banco de dados PostgreSQL');
  }
});

module.exports = pool;