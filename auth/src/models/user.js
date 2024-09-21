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

    static async add(firstname, lastname, email, password, avatar, isActive, isBlocked, isSuper, roles, permissions) {
        const id = uuidv4();
        const query = `
            INSERT INTO tb_user (id, firstname, lastname, email, password, avatar, is_active, is_blocked, is_super) 
            VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9) 
            RETURNING id, firstname, lastname, email, avatar, created_at, updated_at, last_login, is_active, is_blocked, is_super
        `;
        const values = [id, firstname, lastname, email, password, avatar, isActive, isBlocked, isSuper];

        const result = await pool.query(query, values);
        const newUser = result.rows[0];

        // Assign roles to user
        if (roles && roles.length > 0) {
            const roleInsertQuery = `
                INSERT INTO tb_user_role (id, user_id, role_id) 
                VALUES ($1, $2, $3)
            `;
            for (const roleId of roles) {
                const id = uuidv4();
                await pool.query(roleInsertQuery, [id, newUser.id, roleId]);
            }
        }

        // Assign permissions to user
        if (permissions && permissions.length > 0) {
            const permissionInsertQuery = `
                INSERT INTO tb_user_permission (id, user_id, permission_id) 
                VALUES ($1, $2, $3)
            `;
            for (const permissionId of permissions) {
                const id = uuidv4();
                await pool.query(permissionInsertQuery, [id, newUser.id, permissionId]);
            }
        }

        return newUser;
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
            SELECT id, firstname, lastname, email, password, avatar, created_at, updated_at, last_login, otp, otp_expires, is_super, is_active, is_blocked
            FROM tb_user 
            WHERE email = $1
        `;
        const values = [email];

        const result = await pool.query(query, values);
        return result.rows[0];
    }

    static async updateById(id, firstname, lastname, email, avatar, isActive, isBlocked, isSuper, roles, permissions) {
        const updatedAt = Date.now();
            
        const query = `
            UPDATE tb_user 
            SET firstname = $1, lastname = $2, email = $3, avatar = $4, updated_at = $5, is_active = $6, is_blocked = $7, is_super = $8
            WHERE id = $9
            RETURNING id, firstname, lastname, email, avatar, created_at, updated_at, last_login, is_super, is_active, is_blocked
        `;
        const values = [firstname, lastname, email, avatar, updatedAt, isActive, isBlocked, isSuper, id];
    
        const result = await pool.query(query, values);
    
        if (result.rowCount > 0) {
            const updatedUser = result.rows[0];
    
            // Atualizar roles
            await pool.query('DELETE FROM tb_user_role WHERE user_id = $1', [id]);
            if (roles && roles.length > 0) {
                const roleInsertQuery = `
                    INSERT INTO tb_user_role (id, user_id, role_id) 
                    VALUES ($1, $2, $3)
                `;
                for (const roleId of roles) {
                    const roleIdUUID = uuidv4();
                    await pool.query(roleInsertQuery, [roleIdUUID, updatedUser.id, roleId]);
                }
            }
    
            // Atualizar permissions
            await pool.query('DELETE FROM tb_user_permission WHERE user_id = $1', [id]);
            if (permissions && permissions.length > 0) {
                const permissionInsertQuery = `
                    INSERT INTO tb_user_permission (id, user_id, permission_id) 
                    VALUES ($1, $2, $3)
                `;
                for (const permissionId of permissions) {
                    const permissionIdUUID = uuidv4();
                    await pool.query(permissionInsertQuery, [permissionIdUUID, updatedUser.id, permissionId]);
                }
            }
    
            return updatedUser;
        }
        return null;
    }

    static async updatePasswordById(id, newPassword) {
        const updatedAt = Date.now();

        const query = `
            UPDATE tb_user 
            SET updated_at = $1, password = $2
            WHERE id = $3
        `;
        const values = [updatedAt, newPassword, id];

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
            SELECT r.id AS id, r.name AS name
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
            SELECT p.id AS id, p.name AS name
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
            SELECT p.id AS id, p.name AS name
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
            SELECT DISTINCT p.id AS id, p.name AS name
            FROM tb_user u
            JOIN tb_user_role ur ON u.id = ur.user_id
            JOIN tb_role r ON ur.role_id = r.id
            JOIN tb_role_permission rp ON r.id = rp.role_id
            JOIN tb_permission p ON rp.permission_id = p.id
            WHERE u.id = $1
                
            UNION
                
            SELECT DISTINCT p.id AS id, p.name AS name
            FROM tb_user u
            JOIN tb_user_permission up ON u.id = up.user_id
            JOIN tb_permission p ON up.permission_id = p.id
            WHERE u.id = $1
                
            UNION
                
            SELECT DISTINCT p.id AS id, p.name AS name
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

    static async setOtpAndExpires(userId, otp, otpExpires) {
        const query = 'UPDATE tb_user SET otp = $1, otp_expires = $2 WHERE id = $3';
        const values = [otp, otpExpires, userId];
        await pool.query(query, values);
    }

    static async clearOtp(userId) {
        const query = 'UPDATE tb_user SET otp = NULL, otp_expires = NULL WHERE id = $1';
        const values = [userId];
        await pool.query(query, values);
    }
}

module.exports = User;