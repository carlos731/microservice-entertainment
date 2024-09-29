const { validationResult } = require('express-validator');
const User = require('../models/user');
const Role = require('../models/role');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const { uploadFile } = require('../services/uploadService');

const saltRounds = parseInt(process.env.BYCRIPT) || 10;
const secretKey = process.env.BYCRIPT_KEY || 'sua_chave_secreta';
const jwtscretekey = process.env.JWT_SECRET;

class UserController {
    static async addUser(req, res) {
        const { firstname, lastname, email, password, avatar, isActive, isBlocked, isSuper, roles, permissions } = req.body;
        let avatarUrl = null;
        // const formData = new FormData();
        // formData.append('firstname', 'Teste');
        // formData.append('lastname', '123');
        // formData.append('email', 'fds@gmail.com');
        // formData.append('password', 'Teste@123');
        // formData.append('isActive', 'false');
        // formData.append('isBlocked', 'false');
        // formData.append('isSuper', 'false');

        // const avatarUrl = 'http://localhost:9000/storage/image/200x200/bd6a261d-ed30-4454-8cfb-eb2044fdad2d';
        // formData.append('avatar', avatarUrl);

        // const rolesIds = ['387e3b94-dc84-494b-b9fe-7cf12cf25b40', '0f884d18-a3d6-4fb0-a599-d08bc220804e'];
        // rolesIds.forEach(role => formData.append('roles', role));

        // const permissionsIds = ['5737878e-d0e8-466f-b469-a7bce5105113'];
        // permissionsIds.forEach(permission => formData.append('permissions', permission));

        // const firstname = formData.get('firstname');
        // const lastname = formData.get('lastname');
        // const email = formData.get('email');
        // const password = formData.get('password');
        // const avatar = formData.get('avatar');
        // const isActive = formData.get('isActive') === 'true'; // Convertendo para booleano
        // const isBlocked = formData.get('isBlocked') === 'true';
        // const isSuper = formData.get('isSuper') === 'true';

        // const roles = formData.getAll('roles');
        // const permissions = formData.getAll('permissions');

        try {
            const existingUser = await User.findByEmail(email);
            if (existingUser) {
                return res.status(400).json({ error: 'Email já está em uso.' });
            }

            if (req.file) {
                const { originalname, mimetype, buffer } = req.file;
                avatarUrl = await uploadFile({
                    filename: originalname,
                    contentType: mimetype,
                    buffer
                });
            }

            const hashedPassword = await bcrypt.hash(password, saltRounds);
            const newUser = await User.add(firstname, lastname, email, hashedPassword, avatarUrl, isActive, isBlocked, isSuper, roles, permissions);

            const rolesFind = await User.findRolesByUserId(newUser.id);
            const permissionsFind = await User.findAllPermissionsByUserId(newUser.id);

            newUser.roles = rolesFind.map(role => role.name);
            newUser.permissions = permissionsFind.map(permission => permission.name);

            res.status(201).json(newUser);
        } catch (error) {
            console.error(error);
            res.status(500).json({ error: 'Erro ao adicionar usuário.' });
        }
    }

    static async updateUser(req, res) {
        const { id, firstname, lastname, email, avatar, isActive, isBlocked, isSuper, roles, permissions } = req.body;

        try {
            const user = await User.findById(id);
            if (!user) {
                return res.status(404).json({ error: 'Usuário não encontrado.' });
            }

            const existingUser = await User.findByEmail(user.email);
            if (existingUser) {
                return res.status(400).json({ error: 'Email já está em uso.' });
            }

            const updatedUser = await User.updateById(id, firstname, lastname, email, avatar, isActive, isBlocked, isSuper, roles, permissions);
            if (!updatedUser) {
                return res.status(404).json({ error: 'Usuário não encontrado.' });
            }

            const rolesFind = await User.findRolesByUserId(updatedUser.id);
            const permissionsFind = await User.findAllPermissionsByUserId(updatedUser.id);

            updatedUser.roles = rolesFind.map(role => role.name);
            updatedUser.permissions = permissionsFind.map(permission => permission.name);

            res.status(200).json(updatedUser);
        } catch (error) {
            console.error(error);
            res.status(500).json({ error: 'Erro ao atualizar usuário.' });
        }
    }

    static async getAllUsers(req, res) {
        try {
            const users = await User.findAll();
            res.status(200).json(users);
        } catch (error) {
            console.error(error);
            res.status(500).json({ error: 'Erro ao buscar usuários.' });
        }
    }

    static async getUserById(req, res) {
        const { id } = req.params;

        try {
            const user = await User.findById(id);
            if (!user) {
                return res.status(404).json({ error: 'Usuário não encontrado.' });
            }

            const roles = await User.findRolesByUserId(user.id);
            const permissions = await User.findAllPermissionsByUserId(user.id);

            user.roles = roles.map(role => role.name);
            user.permissions = permissions.map(permission => permission.name);
            
            res.status(200).json(user);
        } catch (error) {
            console.error(error);
            res.status(500).json({ error: 'Erro ao buscar usuário.' });
        }
    }

    static async deleteUser(req, res) {
        const { id } = req.params;

        try {
            const deleted = await User.deleteById(id);
            if (!deleted) {
                return res.status(404).json({ error: 'Usuário não encontrado.' });
            }
            res.status(204).send();
        } catch (error) {
            console.error(error);
            res.status(500).json({ error: 'Erro ao deletar usuário.' });
        }
    }
}

module.exports = UserController;