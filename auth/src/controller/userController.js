const { validationResult } = require('express-validator');
const User = require('../models/user');
const Role = require('../models/role');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');

const saltRounds = parseInt(process.env.BYCRIPT) || 10;
const secretKey = process.env.BYCRIPT_KEY || 'sua_chave_secreta';
const jwtscretekey = process.env.JWT_SECRET;

class UserController {
    static async addUser(req, res) {
        const { firstname, lastname, email, password, avatar, isActive, isBlocked, isSuper, roles, permissions } = req.body;

        try {
            const existingUser = await User.findByEmail(email);
            if (existingUser) {
                return res.status(400).json({ error: 'Email já está em uso.' });
            }

            const hashedPassword = await bcrypt.hash(password, saltRounds);
            const newUser = await User.add(firstname, lastname, email, hashedPassword, avatar, isActive, isBlocked, isSuper, roles, permissions);

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