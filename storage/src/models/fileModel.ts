import pool from '../db';

export class FileModel {
  id: string;
  name: string;
  extension: string;
  size: number;
  byte: Uint8Array;
  contentType: string;
  createdAt: number;
  updatedAt?: number;

  constructor(
    id: string,
    name: string,
    extension: string,
    size: number,
    byte: Uint8Array,
    content_type: string,
    createdAt: number,
    updatedAt?: number
  ) {
    this.id = id;
    this.name = name;
    this.extension = extension;
    this.size = size;
    this.byte = byte;
    this.contentType = content_type;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  // Método para criar um arquivo no banco de dados
  static async create(file: FileModel): Promise<void> {
    const query = `
      INSERT INTO tb_files (id, name, extension, size, byte, content_type, created_at, updated_at)
      VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
    `;
    const values = [
      file.id,
      file.name,
      file.extension,
      file.size,
      file.byte,
      file.contentType,
      file.createdAt,
      file.updatedAt
    ];

    await pool.query(query, values);
  }

  // Método para atualizar um arquivo no banco de dados
  static async update(file: FileModel): Promise<void> {
    const query = `
      UPDATE tb_files
      SET name = $2, extension = $3, size = $4, byte = $5, content_type = $6, updated_at = $7
      WHERE id = $1
    `;
    const values = [
      file.id,
      file.name,
      file.extension,
      file.size,
      file.byte,
      file.contentType,
      file.updatedAt || null
    ];

    const result = await pool.query(query, values);

    if (result.rowCount === 0) {
      throw new Error(`File with id ${file.id} not found`);
    }
  }

  // Método para buscar todos os arquivos
  static async findAll(
    page: number = 1,
    size: number = 10
  ): Promise<{ results: FileModel[], pageMetadata: { size: number, totalElements: number, totalPages: number, number: number } }> {
    const offset = (page - 1) * size;

    // Conta o total de elementos
    const countQuery = 'SELECT COUNT(*) FROM tb_files';
    const countResult = await pool.query(countQuery);
    const totalElements = parseInt(countResult.rows[0].count, 10);
    const totalPages = Math.ceil(totalElements / size);

    // Consulta com paginação
    const query = `
      SELECT DISTINCT id, name, extension, size, content_type, created_at, updated_at
      FROM tb_files
      ORDER BY created_at DESC
      LIMIT $1 OFFSET $2
    `;
    const values = [size, offset];
    const result = await pool.query(query, values);

    // Mapeia cada linha para uma instância de FileModel
    const files = result.rows.map((row: any) => new FileModel(
      row.id,
      row.name,
      row.extension,
      row.size,
      row.byte,
      row.content_type,
      row.created_at,
      row.updated_at
    ));

    return {
      results: files,
      pageMetadata: {
        size: size,
        totalElements,
        totalPages,
        number: page
      }
    };
  }

  static async findById(id: string): Promise<FileModel | null> {
    const query = 'SELECT * FROM tb_files WHERE id = $1';
    const result = await pool.query(query, [id]);

    if (result.rows.length > 0) {
      const row = result.rows[0];
      return new FileModel(
        row.id,
        row.name,
        row.extension,
        row.size,
        row.byte,
        row.content_type,
        row.created_at,
        row.updated_at
      );
    } else {
      return null;
    }
  }

  static async deleteById(id: string): Promise<void> {
    const query = 'DELETE FROM tb_files WHERE id = $1';

    try {
      const result = await pool.query(query, [id]);

      if (result.rowCount === 0) {
        throw new Error(`File with id ${id} not found`);
      }
    } catch (error) {
      // Lida com o erro, você pode fazer o log ou re-throw
      console.error(`Failed to delete file with id ${id}:`, error);
      throw error;
    }
  }
}