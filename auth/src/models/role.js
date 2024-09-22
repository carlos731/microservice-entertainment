const pool = require('../config/db');
const { v4: uuidv4 } = require('uuid');

class Role {
    constructor(
        id,
        name
    ) {
        this.id = id;
        this.name = name;
    }

    static async create(name) {
        const id = uuidv4();
        const query = `
            INSERT INTO tb_role (id, name) 
            VALUES ($1, $2) 
            RETURNING id, name
        `;
        const values = [id, name];

        const result = await pool.query(query, values);
        return result.rows[0];
    }

    static async updateById(id, name) {
        const query = `UPDATE tb_role SET name = $1 WHERE id = $2 RETURNING id, name`;
        const values = [name, id];
        const result = await pool.query(query, values);
        return result.rows[0];
    }

    static async findByName(name) {
        const query = 'SELECT id FROM tb_role WHERE name ILIKE $1';
        const values = [name];
        const result = await pool.query(query, values);
        return result.rows[0];
    }

    static async findById(id) {
        const query = 'SELECT * FROM tb_role WHERE id = $1';
        const values = [id];
        const result = await pool.query(query, values);
        return result.rows[0];
    }

    static async findAll() {
        const query = 'SELECT * FROM tb_role';
        const result = await pool.query(query);
        return result.rows;
    }

    static async deleteById(id) {
        const query = 'DELETE FROM tb_role WHERE id = $1';
        const values = [id];
        const result = await pool.query(query, values);
        return result.rowCount > 0;
    }

    static async assignRolesToUser(userId, roleIds) {
        const queries = roleIds.map(roleId => {
            return {
                query: 'INSERT INTO tb_user_role (user_id, role_id) VALUES ($1, $2) ON CONFLICT DO NOTHING',
                values: [userId, roleId]
            };
        });
    
        for (const { query, values } of queries) {
            await pool.query(query, values);
        }
    }

    static async removeRoleFromUser(userId, roleId) {
        const query = 'DELETE FROM tb_user_role WHERE user_id = $1 AND role_id = $2';
        const values = [userId, roleId];
        await pool.query(query, values);
    }
}

module.exports = Role;