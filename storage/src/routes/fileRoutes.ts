import { Router } from 'express';
import multer from 'multer';
import {
    uploadFile,
    uploadMultipleFiles,
    updateFileById,
    getFiles,
    getFileById,
    deleteFileById,
    getVideoById,
    getImageById,
    downloadFileById
} from '../controllers/fileController';
import { authenticateToken, authorizePermissions } from '../middleware/authenticateToken';

const router = Router();

const storage = multer.memoryStorage();
const upload = multer({ storage: storage });

/**
 * @swagger
 * /upload-multiple:
 *   post:
 *     summary: Faz o upload de múltiplos arquivos e retorna as URLs de referência
 *     tags: [Upload File]
 *     security:
 *       - bearerAuth: []
 *     requestBody:
 *       required: true
 *       content:
 *         multipart/form-data:
 *           schema:
 *             type: object
 *             properties:
 *               files:
 *                 type: array
 *                 items:
 *                   type: string
 *                   format: binary
 *                 description: Os arquivos que serão enviados
 *                 collectionFormat: multi
 *             required:
 *               - files
 *     responses:
 *       200:
 *         description: URLs de referência dos arquivos salvos
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 urls:
 *                   type: array
 *                   items:
 *                     type: string
 *                     description: URLs públicas dos arquivos salvos
 *       400:
 *         description: Requisição inválida
 *       500:
 *         description: Erro no servidor
 */
router.post('/upload-multiple', authenticateToken, authorizePermissions('upload:file'), upload.array('files'), uploadMultipleFiles);

/**
 * @swagger
 * /upload:
 *   post:
 *     summary: Faz o upload de um arquivo e retorna a URL de referência
 *     tags: [Upload File]
 *     requestBody:
 *       required: true
 *       content:
 *         multipart/form-data:
 *           schema:
 *             type: object
 *             properties:
 *               file:
 *                 type: string
 *                 format: binary
 *                 description: O arquivo que será enviado
 *     responses:
 *       200:
 *         description: URL de referência ao arquivo salvo
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 url:
 *                   type: string
 *                   description: URL pública do arquivo salvo
 *       400:
 *         description: Requisição inválida (por exemplo, sem arquivo)
 *       500:
 *         description: Erro no servidor
 */
router.post('/upload', /*authenticateToken, authorizePermissions('upload_file'),*/ upload.single('file'), uploadFile);

/**
 * @swagger
 * /update/{id}:
 *   put:
 *     summary: Atualiza um arquivo pelo ID
 *     tags: [Update File]
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: path
 *         name: id
 *         schema:
 *           type: string
 *         required: true
 *         description: ID do arquivo a ser atualizado
 *     requestBody:
 *       required: true
 *       content:
 *         multipart/form-data:
 *           schema:
 *             type: object
 *             properties:
 *               file:
 *                 type: string
 *                 format: binary
 *                 description: O novo arquivo que substituirá o existente
 *             required:
 *               - file
 *     responses:
 *       200:
 *         description: Arquivo atualizado com sucesso
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 url:
 *                   type: string
 *                   description: URL pública do arquivo atualizado
 *       400:
 *         description: Requisição inválida (por exemplo, sem arquivo)
 *       404:
 *         description: Arquivo não encontrado
 *       500:
 *         description: Erro no servidor
 */
router.put('/update/:id', /*authenticateToken, authorizePermissions('update_file'),*/ upload.single('file'), updateFileById);

/**
 * @swagger
 * /files:
 *   get:
 *     summary: Retorna uma lista de arquivos
 *     tags: [Get Files]
 *     parameters:
 *       - in: query
 *         name: page
 *         schema:
 *           type: integer
 *         description: Número da página para paginação
 *       - in: query
 *         name: size
 *         schema:
 *           type: integer
 *         description: Quantidade de resultados por página
 *       - in: query
 *         name: createdAt
 *         schema:
 *           type: string
 *           format: date-time
 *         description: Filtrar por data de criação
 *       - in: query
 *         name: updatedAt
 *         schema:
 *           type: string
 *           format: date-time
 *         description: Filtrar por data de atualização
 *       - in: query
 *         name: sort
 *         schema:
 *           type: string
 *         description: Ordenar por ascendente ou descendente
 *     responses:
 *       200:
 *         description: Lista de arquivos
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 results:
 *                   type: array
 *                   items:
 *                     $ref: '#/components/schemas/File'
 *                 pageMetadata:
 *                   type: object
 *                   properties:
 *                     size:
 *                       type: integer
 *                     totalElements:
 *                       type: integer
 *                     totalPages:
 *                       type: integer
 *                     number:
 *                       type: integer
 *       500:
 *         description: Erro no servidor
 */
router.get('/files', /*authenticateToken,*/ getFiles);

/**
 * @swagger
 * /file/{id}:
 *   get:
 *     summary: Retorna os dados de um arquivo específico
 *     tags: [Get File]
 *     parameters:
 *       - in: path
 *         name: id
 *         schema:
 *           type: string
 *         required: true
 *         description: O ID do arquivo
 *     responses:
 *       200:
 *         description: Dados do arquivo
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/File'
 *       404:
 *         description: Arquivo não encontrado
 *       500:
 *         description: Erro no servidor
 */
router.get('/file/:id', /*authenticateToken,*/ getFileById);

/**
 * @swagger
 * /video/{id}:
 *   get:
 *     summary: Obtém o vídeo por ID
 *     tags: [Get Video]
 *     parameters:
 *       - in: path
 *         name: id
 *         schema:
 *           type: string
 *         required: true
 *         description: O ID do vídeo
 *     responses:
 *       200:
 *         description: Retorna o vídeo
 *         content:
 *           video/mp4:
 *             schema:
 *               type: string
 *               format: binary
 *       404:
 *         description: Vídeo não encontrado
 *       500:
 *         description: Erro no servidor
 */
router.get('/video/:id', /*authenticateToken,*/ getVideoById);

/**
 * @swagger
 * /image/{size}/{id}:
 *   get:
 *     summary: Obtém uma imagem por ID e tamanho
 *     tags: [Get Image]
 *     parameters:
 *       - in: path
 *         name: id
 *         schema:
 *           type: string
 *         required: true
 *         description: O ID da imagem
 *       - in: path
 *         name: size
 *         schema:
 *           type: string
 *         description: O tamanho da imagem (pequena, média, grande)
 *     responses:
 *       200:
 *         description: Retorna a imagem
 *         content:
 *           image/jpeg:
 *             schema:
 *               type: string
 *               format: binary
 *       404:
 *         description: Imagem não encontrada
 *       500:
 *         description: Erro no servidor
 */
router.get('/image/:size?/:id', /*authenticateToken,*/ getImageById);

/**
 * @swagger
 * /download/{id}:
 *   get:
 *     summary: Faz o download de um arquivo por ID
 *     tags: [Download File]
 *     parameters:
 *       - in: path
 *         name: id
 *         schema:
 *           type: string
 *         required: true
 *         description: O ID do arquivo a ser baixado
 *     responses:
 *       200:
 *         description: Arquivo baixado com sucesso
 *         content:
 *           application/octet-stream:
 *             schema:
 *               type: string
 *               format: binary
 *       404:
 *         description: Arquivo não encontrado
 *       500:
 *         description: Erro no servidor
 */
router.get('/download/:id', downloadFileById);

/**
 * @swagger
 * /delete/{id}:
 *   delete:
 *     summary: Deleta um arquivo pelo ID (necessita de autenticação)
 *     tags: [Delete File]
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: string
 *         description: ID do arquivo que será deletado
 *     responses:
 *       200:
 *         description: Arquivo deletado com sucesso
 *       401:
 *         description: Não autorizado (token inválido ou ausente)
 *       404:
 *         description: Arquivo não encontrado
 *       500:
 *         description: Erro no servidor
 */
router.delete('/delete/:id', authenticateToken, authorizePermissions('delete:file'), deleteFileById);

/**
 * @swagger
 * components:
 *   securitySchemes:
 *     bearerAuth:
 *       type: http
 *       scheme: bearer
 *       bearerFormat: JWT
 *   schemas:
 *     File:
 *       type: object
 *       properties:
 *         id:
 *           type: string
 *         name:
 *           type: string
 *         extension:
 *           type: string
 *         size:
 *           type: integer
 *         content_type:
 *           type: string
 *         createdAt:
 *           type: string
 *           format: date-time
 *         updatedAt:
 *           type: string
 *           format: date-time
 */

export default router;