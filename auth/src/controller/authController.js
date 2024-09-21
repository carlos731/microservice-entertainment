const { validationResult } = require('express-validator');
const User = require('../models/user');
const Role = require('../models/role');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');

const saltRounds = parseInt(process.env.BYCRIPT) || 10;
const secretKey = process.env.BYCRIPT_KEY || 'sua_chave_secreta';
const jwtscretekey = process.env.JWT_SECRET;

class AuthController {
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

            user.roles = roles.map(role => role.name);
            user.permissions = permissions.map(permission => permission.name);

            const tokenPayload = {
                sub: user.id,
                is_super: user.is_super,
                roles: roles.map(role => role.name),
                permissions: permissions.map(permission => permission.name)
            };

            const accessToken = jwt.sign(tokenPayload, jwtscretekey, { expiresIn: '15m' });
            const refreshToken = jwt.sign({ sub: user.id }, jwtscretekey, { expiresIn: '7d' });

            res.status(200).json({ user, accessToken, refreshToken });
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
                roles: roles.map(role => role.name),
                permissions: permissions.map(permission => permission.name)
            };

            const accessToken = jwt.sign(tokenPayload, jwtscretekey, { expiresIn: '15m' });
            const refreshToken = jwt.sign({ sub: user.id }, jwtscretekey, { expiresIn: '7d' });

            res.status(200).json({ /*user,*/ accessToken, refreshToken });
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
                sub: userFind.id,
                is_super: userFind.is_super,
                roles: roles.map(role => role.name),
                permissions: permissions.map(permission => permission.name)
            };

            const accessToken = jwt.sign(tokenPayload, jwtscretekey, { expiresIn: '15m' });
            const refreshToken = jwt.sign({ sub: userFind.id }, jwtscretekey, { expiresIn: '7d' });

            res.status(200).json({
                accessToken: accessToken,
                refreshToken: refreshToken,
            });
        });
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

    static async updatePassword(req, res) {
        const { id } = req.user;
        const { currentPassword, newPassword } = req.body;

        try {
            const user = await User.findById(id);
            if (!user) {
                return res.status(404).json({ error: 'Usuário não encontrado.' });
            }

            const match = await bcrypt.compare(currentPassword, user.password);
            if (!match) {
                return res.status(401).json({ error: 'Senha atual incorreta.' });
            }

            const hashedPassword = await bcrypt.hash(newPassword, saltRounds);
            await User.updateById(id, user.firstname, user.lastname, user.email, hashedPassword);

            res.status(200).json({ message: 'Senha atualizada com sucesso.' });
        } catch (error) {
            console.error(error);
            res.status(500).json({ error: 'Erro ao atualizar a senha.' });
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

    static async forgotPassword(req, res) {
        const { email } = req.body;

        try {
            const user = await User.findByEmail(email);
            if (!user) {
                return res.status(404).json({ error: 'Usuário não encontrado.' });
            }

            const { otp } = await AuthController.generateOtp(user.id);

            res.status(200).json({ message: 'OTP enviado para o e-mail.', otp });
        } catch (error) {
            console.error(error);
            res.status(500).json({ error: 'Erro ao enviar OTP.' });
        }
    }

    static async resetPassword(req, res) {
        const { newPassword } = req.body;
        const email = req.tokenDecoded.email;
        console.log(email);

        try {
            const user = await User.findByEmail(email);
            if (!user) {
                return res.status(404).json({ error: 'Usuário não encontrado.' });
            }

            const hashedPassword = await bcrypt.hash(newPassword, saltRounds);
            await User.updatePasswordById(user.id, hashedPassword);

            await User.clearOtp(user.id);

            res.status(200).json({ message: 'Senha redefinida com sucesso.' });
        } catch (error) {
            console.error(error);
            res.status(500).json({ error: 'Erro ao redefinir a senha.' });
        }
    }

    static async verifyOtp(req, res) {
        const { email, otp } = req.body;

        try {
            const user = await User.findByEmail(email);
            if (!user) {
                return res.status(404).json({ error: 'Usuário não encontrado.' });
            }

            if (user.otp !== otp) {
                return res.status(400).json({ error: 'OTP inválido.' });
            }

            const currentTime = Date.now();

            if (currentTime > user.otpExpires) {
                return res.status(400).json({ error: 'OTP expirado.' });
            }

            const tokenPayload = { email: user.email };
            const accessToken = jwt.sign(tokenPayload, jwtscretekey, { expiresIn: '15m' });

            await User.clearOtp(user.id);

            res.status(200).json({ message: 'OTP válido.', accessToken });
        } catch (error) {
            console.error(error);
            res.status(500).json({ error: 'Erro ao verificar OTP.' });
        }
    }

    static async generateOtp(userId) {
        try {
            const otp = Math.floor(100000 + Math.random() * 900000).toString();
            const otpExpires = Date.now() + 15 * 60 * 1000; // Expira em 15 minutos

            await User.setOtpAndExpires(userId, otp, otpExpires);

            // await sendOTPEmail(email, otp); // enviar para o email

            console.log(`OTP gerado para o usuário ID ${userId}: ${otp}`);

            return { otp, otpExpires };
        } catch (error) {
            console.error(error);
            throw new Error('Erro ao gerar OTP.');
        }
    }
}

module.exports = AuthController;