const pool = require('../config/db');
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

    static async findAll() {
        const query = "SELECT * FROM tb_permission";
        const result = await pool.query(query);
        console.log('Query Result:', result.rows);
        return result.rows;
    }
}

module.exports = Permission;