const { con } = require('../connection');

// Função auxiliar para Promissificar con.query (necessário se 'con' não for um objeto com suporte nativo a Promises)
function queryPromise(sql) {
    return new Promise((resolve, reject) => {
        con.query(sql, (err, result) => {
            if (err) {
                return reject(err);
            }const { con } = require('../connection'); // Note the destructuring to get 'con'

function login(data, callback){ 
    console.log("Attempting login...");
    let sql = `select * from tb_teste where email='${data.email}' and senha='${data.senha}'`;
    
    con.query(sql, function (err, result) { 
        if (err) {
            // Se houver erro, chame o callback com o erro
            console.error("Database query error:", err);
            return callback(err, null);
        }
        
        // Se não houver erro, chame o callback com o resultado (resultset)
        console.log(`Query successful. Rows found: ${result.length}`);
        return callback(null, result); 
    });
}

            resolve(result);
        });
    });
}

// A função login agora é assíncrona e retorna uma Promise (via queryPromise)
async function login(data){
    console.log("Attempting login...");
    
    // A query será executada, e a Promise retornada será resolvida/rejeitada
    let sql = `select * from tb_usuarios where email='${data.email}' and senha='${data.senha}'`;
    
    return queryPromise(sql); 
    // O await no server.js vai pegar o resultado dessa Promise.
}

async function cadastro(data){
    console.log("Attempting user registration...");
    
    // Query de inserção. É crucial usar placeholders '?' aqui.
    let sql = `INSERT INTO tb_usuarios (nome, email, senha) VALUES ('${data.nome}', '${data.email}', '${data.senha}')`;
    
    // Os dados são passados como um array. O driver MySQL substitui os '?' pelos valores de forma segura.
    return queryPromise(sql, [data.nome, data.email, data.senha]);
}


module.exports = {
    login,
    cadastro
}