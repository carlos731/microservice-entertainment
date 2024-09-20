const pool = require('../config/db');
const { v4: uuidv4 } = require('uuid');

class User {
    constructor(
        id,
        firstname,
        lastname,
        email,
        password,
        avatar,
        createdAt,
        updatedAt,
        lastLogin,
        otpExpires,
        otp,
        isActive,
        isBlocked,
        isSuper
    ) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastLogin = lastLogin;
        this.otpExpires = otpExpires;
        this.otp = otp;
        this.isActive = isActive;
        this.isBlocked = isBlocked;
        this.isSuper = isSuper;
    }

    static async register(firstname, lastname, email, password, avatar) {
        const id = uuidv4();

        const query = `
            INSERT INTO tb_user (id, firstname, lastname, email, password, avatar) 
            VALUES ($1, $2, $3, $4, $5, $6) 
            RETURNING id, firstname, lastname, email, avatar, created_at, updated_at, last_login, is_active, is_blocked, is_super
        `;
        const values = [id, firstname, lastname, email, password, avatar];

        const result = await pool.query(query, values);
        return result.rows[0];
    }

    static async findAll() {
        const query = "SELECT * FROM tb_user";
        const result = await pool.query(query);
        return result.rows;
    }

    static async findById(id) {
        const query = `
            SELECT id, firstname, lastname, email, password, avatar, created_at, updated_at, last_login, is_super, is_active, is_blocked
            FROM tb_user 
            WHERE id = $1
        `;
        const values = [id];

        const result = await pool.query(query, values);
        return result.rows[0];
    }

    static async findByEmail(email) {
        const query = `
            SELECT id, firstname, lastname, email, password, avatar, created_at, updated_at, last_login, is_super, is_active, is_blocked
            FROM tb_user 
            WHERE email = $1
        `;
        const values = [email];

        const result = await pool.query(query, values);
        return result.rows[0];
    }

    static async updateById(id, firstname, lastname, email, password) {
        const query = `
            UPDATE tb_user 
            SET firstname = $1, lastname = $2, email = $3, password = $4 
            WHERE id = $5
            RETURNING id, firstname, lastname, email, avatar, created_at, updated_at, last_login, is_super, is_active, is_blocked
        `;
        const values = [firstname, lastname, email, password, id];

        const result = await pool.query(query, values);

        if (result.rowCount > 0) {
            return result.rows[0];
        }
        return null;
    }

    static async deleteById(id) {
        const query = 'DELETE FROM tb_user WHERE id = $1';
        const values = [id];

        const result = await pool.query(query, values);
        return result.rowCount > 0;
    }

    static async findRolesByUserId(userId) {
        const query = `
            SELECT r.name AS role_name
            FROM tb_user u
            JOIN tb_user_role ur ON u.id = ur.user_id
            JOIN tb_role r ON ur.role_id = r.id
            WHERE u.id = $1
        `;
        const values = [userId];

        const result = await pool.query(query, values);
        return result.rows;
    }

    static async findPermissionsByRoleId(roleId) {
        const query = `
            SELECT p.name AS permission_name
            FROM tb_role r
            JOIN tb_role_permission rp ON r.id = rp.role_id
            JOIN tb_permission p ON rp.permission_id = p.id
            WHERE r.id = $1
        `;
        const values = [roleId];

        const result = await pool.query(query, values);
        return result.rows;
    }

    static async findPermissionsByUserId(userId) {
        const query = `
            SELECT p.name AS permission_name
            FROM tb_user u
            JOIN tb_user_permission up ON u.id = up.user_id
            JOIN tb_permission p ON up.permission_id = p.id
            WHERE u.id = $1
        `;
        const values = [userId];

        const result = await pool.query(query, values);
        return result.rows;
    }

    static async findAllPermissionsByUserId(userId) {
        const query = `
            SELECT DISTINCT p.name AS permission_name
            FROM tb_user u
            JOIN tb_user_role ur ON u.id = ur.user_id
            JOIN tb_role r ON ur.role_id = r.id
            JOIN tb_role_permission rp ON r.id = rp.role_id
            JOIN tb_permission p ON rp.permission_id = p.id
            WHERE u.id = $1
                
            UNION
                
            SELECT DISTINCT p.name AS permission_name
            FROM tb_user u
            JOIN tb_user_permission up ON u.id = up.user_id
            JOIN tb_permission p ON up.permission_id = p.id
            WHERE u.id = $1
                
            UNION
                
            SELECT DISTINCT p.name AS permission_name
            FROM tb_permission p
            WHERE EXISTS (
                SELECT 1 
                FROM tb_user 
                WHERE id = $1 AND is_super = true
            )
        `;
        const values = [userId];

        const result = await pool.query(query, values);
        return result.rows;
    }
}

module.exports = User;