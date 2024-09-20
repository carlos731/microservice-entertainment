const { validationResult } = require('express-validator');
const User = require('../models/user');
const Role = require('../models/role');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');

const saltRounds = parseInt(process.env.BYCRIPT) || 10;
const secretKey = process.env.BYCRIPT_KEY || 'sua_chave_secreta';
const jwtscretekey = process.env.JWT_SECRET;

class UserController {
    static async register(req, res) {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            const errorMessages = errors.array().map((error) => ({
                field: error.path,
                message: error.msg,
            }));
            return res.status(400).json({ errors: errorMessages });
        }

        const { firstname, lastname, email, password, avatar } = req.body;

        try {
            const existingUser = await User.findByEmail(email);
            if (existingUser) {
                return res.status(400).json({ error: 'Email já está em uso.' });
            }

            const hashedPassword = await bcrypt.hash(password, saltRounds);
            const user = await User.register(firstname, lastname, email, hashedPassword, avatar);

            const roleUser = await Role.findByName('User');
            //const roleAdmin = await Role.findByName('Admin');
            if (roleUser) {
                await Role.assignRolesToUser(user.id, [roleUser.id/*, roleAdmin.id*/]);
            }

            const roles = await User.findRolesByUserId(user.id);
            const permissions = await User.findAllPermissionsByUserId(user.id);

            const tokenPayload = {
                id: user.id,
                is_super: user.is_super,
                roles: roles.map(role => role.role_name),
                permissions: permissions.map(permission => permission.permission_name)
            };

            const token = jwt.sign(tokenPayload, jwtscretekey, { expiresIn: '15m' });

            res.status(201).json({ user, token });
        } catch (error) {
            console.error(error);
            res.status(500).json({ error: 'Erro ao registrar usuário.' });
        }
    }

    static async login(req, res) {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            const errorMessages = errors.array().map((error) => ({
                field: error.path,
                message: error.msg,
            }));
            return res.status(400).json({ errors: errorMessages });
        }

        const { email, password } = req.body;

        try {
            const user = await User.findByEmail(email);
            if (!user) {
                return res.status(404).json({ error: 'Incorrect username or password.' });
            }

            const match = await bcrypt.compare(password, user.password);
            if (!match) {
                return res.status(401).json({ error: 'Incorrect username or password.' });
            }

            const roles = await User.findRolesByUserId(user.id);
            const permissions = await User.findAllPermissionsByUserId(user.id);

            const tokenPayload = {
                sub: user.id,
                is_super: user.is_super,
                roles: roles.map(role => role.role_name),
                permissions: permissions.map(permission => permission.permission_name)
            };

            const token = jwt.sign(tokenPayload, jwtscretekey, { expiresIn: '15m' });
            const refreshToken = jwt.sign({ sub: user.id }, jwtscretekey, { expiresIn: '7d' });

            res.status(200).json({ /*user,*/ token, refreshToken });
        } catch (error) {
            console.error(error);
            res.status(500).json({ error: 'Erro ao realizar login.' });
        }
    }

    static async refreshToken(req, res) {
        const { token } = req.body;

        if (!token) {
            return res.status(403).json({ error: 'Refresh token não fornecido.' });
        }

        jwt.verify(token, jwtscretekey, async (err, user) => {
            if (err) {
                return res.status(403).json({ error: 'Refresh token inválido.' });
            }

            const userFind = await User.findById(user.sub);
            if (!userFind) {
                return res.status(404).json({ error: 'Incorrect username or password.' });
            }

            const roles = await User.findRolesByUserId(userFind.id);
            const permissions = await User.findAllPermissionsByUserId(userFind.id);

            const tokenPayload = {
                id: userFind.id,
                is_super: userFind.is_super,
                roles: roles.map(role => role.role_name),
                permissions: permissions.map(permission => permission.permission_name)
            };

            const newAccessToken = jwt.sign(tokenPayload, jwtscretekey, { expiresIn: '15m' });
            const newRefreshToken = jwt.sign({ sub: user.id }, jwtscretekey, { expiresIn: '7d' });

            res.status(200).json({
                accessToken: newAccessToken,
                refreshToken: newRefreshToken,
            });
        });
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
            res.status(200).json(user);
        } catch (error) {
            console.error(error);
            res.status(500).json({ error: 'Erro ao buscar usuário.' });
        }
    }

    static async updateUser(req, res) {
        const { id } = req.params;
        const { firstname, lastname, email, password } = req.body;

        try {
            const updatedUser = await User.updateById(id, firstname, lastname, email, password);
            if (!updatedUser) {
                return res.status(404).json({ error: 'Usuário não encontrado.' });
            }
            res.status(200).json(updatedUser);
        } catch (error) {
            console.error(error);
            res.status(500).json({ error: 'Erro ao atualizar usuário.' });
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

    static verifyToken(req, res) {
        const token = req.headers['authorization']?.split(' ')[1];

        if (!token) {
            return res.status(403).json({ error: 'Token não fornecido.' });
        }

        jwt.verify(token, jwtscretekey, (err, decoded) => {
            if (err) {
                return res.status(401).json({ error: 'Token inválido.' });
            }

            res.status(200).json({ message: 'Token válido', userId: decoded.id });
        });
    }
}

module.exports = UserController;