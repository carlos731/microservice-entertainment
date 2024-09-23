// const pool = require('../config/db');
const { getPool } = require('../config/db');
const { v4: uuidv4 } = require('uuid');

class Permission {
    constructor(
        id,
        name,
        roles,
    ) {
        this.id = id;
        this.name = name;
        this.roles = roles;
    }

    static async create(name, description) {
        const id = uuidv4();
        const query = `
            INSERT INTO tb_permission (id, name, description) 
            VALUES ($1, $2, $3) 
            RETURNING id, name, description
        `;
        const values = [id, name, description];
        const pool = getPool();
        const result = await pool.query(query, values);
        return result.rows[0];
    }

    static async updateById(id, name, description) {
        const query = `UPDATE tb_permission SET name = $1, description = $2 WHERE id = $3 RETURNING id, name, description`;
        const values = [name, description, id];
        const pool = getPool();
        const result = await pool.query(query, values);
        return result.rows[0];
    }

    static async findByName(name) {
        const query = 'SELECT id FROM tb_permission WHERE name ILIKE $1';
        const values = [name];
        const pool = getPool();
        const result = await pool.query(query, values);
        return result.rows[0];
    }

    static async findById(id) {
        const query = 'SELECT * FROM tb_permission WHERE id = $1';
        const values = [id];
        const pool = getPool();
        const result = await pool.query(query, values);
        return result.rows[0];
    }

    static async findAll() {
        const query = 'SELECT * FROM tb_permission';
        const pool = getPool();
        const result = await pool.query(query);
        return result.rows;
    }

    static async deleteById(id) {
        const query = 'DELETE FROM tb_permission WHERE id = $1';
        const values = [id];
        const pool = getPool();
        const result = await pool.query(query, values);
        return result.rowCount > 0;
    }
}

module.exports = Permission;