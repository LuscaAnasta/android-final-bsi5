const express = require('express');
const usuario = require('./controller/usuarioController');
const diario = require('./controller/diarioController');
const alimento = require('./controller/alimentoController');

const app = express();
const port = 8066;

app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// =========================
// LOGIN
// =========================
app.get('/usuario/login', async (req, res) => {
    const data = req.query;

    try {
        const resultSet = await usuario.login(data);

        if (resultSet.length > 0) {
            return res.status(200).send({
                message: "Login bem-sucedido!",
                user: resultSet[0]
            });
        } else {
            return res.status(401).send({ message: "Credenciais inválidas." });
        }
    } catch (err) {
        return res.status(500).send({ message: "Erro no login." });
    }
});

// ========================
// CADASTRO
// ========================
app.post('/usuario/cadastro', async (req, res) => {
    const data = req.body;

    try {
        const result = await usuario.cadastro(data);

        if (result.affectedRows > 0) {
            return res.status(201).send({
                message: "Usuário cadastrado com sucesso!",
                id: result.insertId
            });
        }
    } catch (err) {
        return res.status(500).send({ message: "Erro ao cadastrar usuário." });
    }
});

// ========================
// DIARIO - LISTAR GRUPOS DO DIA
// ========================
app.get('/diario/grupos', async (req, res) => {
    try {
        const result = await diario.listarGrupos(req.query);
        return res.status(200).send(result);
    } catch (err) {
        return res.status(500).send({ message: "Erro ao listar grupos." });
    }
});

// ========================
// DIARIO - LISTAR ALIMENTOS DE UM GRUPO
// ========================
app.get('/diario/grupo/alimentos', async (req, res) => {
    try {
        const result = await diario.listarAlimentos(req.query.grupo_id);
        return res.status(200).send(result);
    } catch (err) {
        return res.status(500).send({ message: "Erro ao listar alimentos." });
    }
});

// ========================
// DIARIO - CRIAR GRUPO
// ========================
app.post('/diario/grupo', async (req, res) => {
    try {
        const result = await diario.criarGrupo(req.body);
        return res.status(201).send({ message: "Grupo criado!", id: result.insertId });
    } catch (err) {
        return res.status(500).send({ message: "Erro ao criar grupo." });
    }
});

// ========================
// DIARIO - APAGAR GRUPO
// ========================
app.delete('/diario/grupo/:id', async (req, res) => {
    try {
        await diario.apagarGrupo(req.params.id);
        return res.status(200).send({ message: "Grupo apagado!" });
    } catch (err) {
        return res.status(500).send({ message: "Erro ao apagar grupo." });
    }
});

// ========================
// ALIMENTO - ADICIONAR
// ========================
app.post('/alimento', async (req, res) => {
    try {
        const result = await alimento.adicionarAlimento(req.body);
        return res.status(201).send({ message: "Alimento inserido!", id: result.insertId });
    } catch (err) {
        return res.status(500).send({ message: "Erro ao inserir alimento." });
    }
});

// ========================
// ALIMENTO - EDITAR
// ========================
app.put('/alimento/:id', async (req, res) => {
    try {
        await alimento.editarAlimento(req.params.id, req.body);
        return res.status(200).send({ message: "Alimento atualizado!" });
    } catch (err) {
        return res.status(500).send({ message: "Erro ao editar alimento." });
    }
});

app.get('/diario/grupo/:id/alimentos', async (req, res) => {
    try {
        const result = await diario.listarAlimentos(req.params.id);
        return res.status(200).send(result);
    } catch (err) {
        return res.status(500).send({ message: "Erro ao listar alimentos." });
    }
});

// ========================
// ALIMENTO - APAGAR
// ========================
app.delete('/alimento/:id', async (req, res) => {
    try {
        await alimento.apagarAlimento(req.params.id);
        return res.status(200).send({ message: "Alimento apagado!" });
    } catch (err) {
        return res.status(500).send({ message: "Erro ao apagar alimento." });
    }
});


app.get('/diario/:usuarioId/:data', async (req, res) => {
    const usuario_id = req.params.usuarioId;
    const data = req.params.data;

    try {
        const result = await diario.listarGrupos({ usuario_id, data });
        return res.status(200).send(result);
    } catch (err) {
        console.error(err);
        return res.status(500).send({ message: "Erro ao listar grupos." });
    }
});

// ========================
// DIARIO - CRIAR GRUPO (para Android)
// ========================
app.post('/diario/:usuarioId/grupo', async (req, res) => {
    const usuario_id = req.params.usuarioId;
    const { nome } = req.body;

    try {
        const result = await diario.criarGrupo({ usuario_id, nome });
        return res.status(201).send(result); // envia todo o objeto criado
    } catch (err) {
        console.error(err);
        return res.status(500).send({ message: "Erro ao criar grupo." });
    }
});

app.get('/alimentos/pesquisa', async (req, res) => {
    try {
        const termo = req.query.q; // ex: ?q=arroz
        if (!termo) return res.status(400).send({ message: "Termo de busca vazio." });
        
        const result = await alimento.buscarAlimentosBase(termo);
        return res.status(200).send(result);
    } catch (err) {
        return res.status(500).send({ message: "Erro ao buscar alimentos." });
    }
});

app.get('/grupo/:id', async (req, res) => {
    try {
        const result = await diario.buscarGrupoPorId(req.params.id);
        
        if (result.length > 0) {
            // Retorna apenas o objeto (o primeiro da lista), pois o Android espera um objeto, não um array
            return res.status(200).send(result[0]); 
        } else {
            return res.status(404).send({ message: "Grupo não encontrado." });
        }
    } catch (err) {
        console.error(err); // Bom para debug no terminal
        return res.status(500).send({ message: "Erro ao buscar grupo." });
    }
});

app.listen(port, () => {
    console.log(`API rodando: http://localhost:${port}`);
});
