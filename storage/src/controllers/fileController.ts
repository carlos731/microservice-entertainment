import { Request, Response } from 'express';
import { FileModel } from '../models/fileModel';
import { v4 as uuidv4 } from 'uuid';
import path from 'path';
// fs.writeFileSync('exemplo.txt', 'Conteúdo do arquivo', 'utf8');
import fs from 'fs'; // gerenciar diretorios do pc
import { format } from 'date-fns'; // para formatar datas ano, mes
// import ffmpeg from 'fluent-ffmpeg'; // usar para redimensionar videos e mexer na aqualidade
import sharp from 'sharp';

export const uploadMultipleFiles = async (req: Request, res: Response) => {
    try {
        if (!req.files || (req.files as Express.Multer.File[]).length === 0) {
            return res.status(400).json({ error: 'Nenhum arquivo enviado' });
        }

        const files = req.files as Express.Multer.File[];
        const fileModels = files.map(file => new FileModel(
            uuidv4(), // Gera um ID único
            file.originalname,
            file.originalname.split('.').pop() || '',
            file.size,
            new Uint8Array(file.buffer),
            file.mimetype,
            new Date()
        ));

        await Promise.all(fileModels.map(file => FileModel.create(file)));

        res.status(200).json({ message: 'Arquivos enviados e salvos com sucesso' });
    } catch (error) {
        console.error('Erro ao fazer upload dos arquivos', error);
        res.status(500).json({ error: 'Falha ao fazer upload dos arquivos' });
    }
};

export const uploadFile = async (req: Request, res: Response) => {
    try {
        if (!req.file) {
            return res.status(400).json({ error: 'Nenhum arquivo enviado' });
        }

        const { originalname, mimetype, size, buffer } = req.file;

        const newFile = new FileModel(
            uuidv4(), // Gera um ID único
            originalname,
            originalname.split('.').pop() || '',
            size,
            new Uint8Array(buffer),
            mimetype,
            new Date()
        );

        await FileModel.create(newFile);

        const baseUrl = 'http://localhost:9000/storage/';
        const fileType = mimetype.startsWith('image/') ? 'image' : 'video';
        const url = `${baseUrl}${fileType}/${newFile.id}`;

        res.status(200).json({
            url
        });
    } catch (error) {
        console.error('Erro ao fazer upload do arquivo', error);
        res.status(500).json({ error: 'Falha ao fazer upload do arquivo' });
    }
};

export const getFileById = async (req: Request, res: Response) => {
    try {
        const fileId = req.params.id;
        const file = await FileModel.findById(fileId);

        if (!file) {
            return res.status(404).json({ message: 'Arquivo não encontrado' });
        }

        res.status(200).json(file);
    } catch (error) {
        console.error('Erro ao buscar o arquivo', error);
        res.status(500).json({ message: 'Falha ao buscar o arquivo' });
    }
};

export const getFiles = async (req: Request, res: Response) => {
    try {
        const page = parseInt(req.query.page as string) || 1;
        const size = parseInt(req.query.size as string) || 10;

        const { results, pageMetadata } = await FileModel.findAll(page, size);
        res.status(200).json({
            results,
            pageMetadata
        });
    } catch (error) {
        console.error('Error fetching files:', error);
        res.status(500).json({ message: 'Error fetching files', error });
    }
};

export const deleteFileById = async (req: Request, res: Response) => {
    try {
        const fileId = req.params.id;

        if (!fileId) {
            return res.status(400).json({ error: 'File ID not provided' });
        }

        const file = await FileModel.findById(fileId);

        if (!file) {
            return res.status(404).json({ error: 'File not found' });
        }

        await FileModel.deleteById(fileId);

        res.status(200).json({ message: 'File deleted successfully' });
    } catch (error) {
        console.error('Failed to delete the file', error);
        res.status(500).json({ error: 'Failed to delete the file' });
    }
};

export const getVideoById = async (req: Request, res: Response) => {
    try {
        const fileId = req.params.id as string;
        if (!fileId) {
            return res.status(400).json({ error: 'ID do vídeo não fornecido' });
        }

        const file = await FileModel.findById(fileId);

        if (!file) {
            return res.status(404).json({ error: 'Video not found' });
        }

        if (!file.contentType.startsWith('video/')) {
            return res.status(400).json({ error: 'Arquivo não é um vídeo' });
        }

        res.setHeader('Content-Type', file.contentType);
        res.send(file.byte);
    } catch (error) {
        console.error('Erro ao retornar o vídeo', error);
        res.status(500).json({ error: 'Falha ao retornar o vídeo' });
    }
};

export const getImageById = async (req: Request, res: Response) => {
    try {
        const fileId = req.params.id;
        const size = req.params.size;

        if (!fileId) {
            return res.status(400).json({ error: 'ID da imagem não fornecido' });
        }

        const file = await FileModel.findById(fileId);

        if (!file) {
            return res.status(404).json({ error: 'Image not found' });
        }

        const supportedTypes = ['image/png', 'image/jpeg'];
        if (!supportedTypes.includes(file.contentType)) {
            return res.status(400).json({ error: 'Tipo de arquivo não suportado' });
        }

        let imageBuffer = file.byte;

        if (size && size !== 'original') {
            const [width, height] = size.split('x').map(dim => parseInt(dim, 10));

            if (!isNaN(width) && !isNaN(height)) {
                try {
                    imageBuffer = await sharp(file.byte)
                        .resize(width, height)
                        .toBuffer();
                } catch (error) {
                    console.error('Erro ao redimensionar a imagem', error);
                    return res.status(500).json({ error: 'Falha ao redimensionar a imagem' });
                }
            } else {
                return res.status(400).json({ error: 'Parâmetros de tamanho inválidos' });
            }
        }

        res.setHeader('Content-Type', file.contentType);
        res.send(imageBuffer);
    } catch (error) {
        console.error('Erro ao retornar a imagem', error);
        res.status(500).json({ error: 'Falha ao retornar a imagem' });
    }
};

export const downloadFileById = async (req: Request, res: Response) => {
    try {
        const fileId = req.params.id;

        if (!fileId) {
            return res.status(400).json({ error: 'ID do arquivo não fornecido' });
        }

        const file = await FileModel.findById(fileId);

        if (!file) {
            return res.status(404).json({ error: 'Arquivo não encontrado' });
        }

        const fileExtension = file.extension ? `.${file.extension}` : '';
        res.setHeader('Content-Disposition', `attachment; filename=${file.id}${fileExtension}`);
        res.setHeader('Content-Type', file.contentType);
        res.send(file.byte);
    } catch (error) {
        console.error('Erro ao fazer o download do arquivo', error);
        res.status(500).json({ error: 'Falha ao fazer o download do arquivo' });
    }
};