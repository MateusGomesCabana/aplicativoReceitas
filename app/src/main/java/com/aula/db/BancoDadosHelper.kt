import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*
import com.aula.constants.RECEITAS_DB_NAME

class BancoDadosHelper(context: Context) :
    ManagedSQLiteOpenHelper(ctx = context , name = "$RECEITAS_DB_NAME.db",  version = 2) {
    //ela executa quando o aplicativo rodas
//    private val scriptSQLCreate = arrayOf(
//        "INSERT INTO $RECEITAS_DB_NAME VALUES(1,'fernando.collor@gmail.com','Fernando Collor','R. Assungui, 27, Cursino, São Paulo, 04131-000, Brasil',800200300,NULL,'www.google.com.br',NULL);",
//        "INSERT INTO $RECEITAS_DB_NAME VALUES(2,'dilma@gmail.com','Dilma','R. José Cocciuffo, 90 - Cursino, São Paulo, 04121-120, Brasil',800235468,NULL,'www.uol.com.br',NULL);",
//        "INSERT INTO $RECEITAS_DB_NAME VALUES(3,'lula@gmail.com','Lula','R. José Cocciuffo, 56 - Cursino, São Paulo, 04121-120, Brasil',80023587,NULL,'www.google.com',NULL);",
//        "INSERT INTO $RECEITAS_DB_NAME VALUES(4,'maluf@gmail.com','Maluf','R. Camilo José, 48 - Cursino, São Paulo, 04125-140, Brasil',800025774,NULL,'www.uol.com.br',NULL);")
    private val scriptSQLCreate = arrayOf(
            "INSERT INTO $RECEITAS_DB_NAME (id, nome, autor, email, descricao ,ingredientes, modopreparo, foto) VALUES (1,'Bolo de chocolate','Mateus', 'mateus@teste.com', 'Um bolo gostoso','1 xícara (chá) de leite;1 xícara (chá) de óleo;2 xícara (chá) de farinha de trigo;1 xícara (chá) de chocolate ou achocolatado;1 xícara (chá) de açúcar;2 ovos;1 colher (sopa) de fermento em pó','Bata no liquidificador os ovos por 5 minutos.\n" +
                    "Acrescente aos poucos os demais ingredientes, exceto o fermento.\n" +
                    "Depois de bater acrescente então o fermento.\n" +
                    "Coloque em uma forma média untada e enfarinhada.\n" +
                    "Leve para assar em forno médio, pré-aquecido por 45 minutos, ou até dourar.',NULL);",
            "INSERT INTO $RECEITAS_DB_NAME (id, nome, autor, email, descricao ,ingredientes, modopreparo, foto) VALUES (2,'Bolo de cenoura','Mateus', 'mateus@teste.com', 'Bolo delicioso','1/2 xícara (chá) de óleo;3 cenouras médias raladas;4 ovos;2 xícaras (chá) de açúcar;2 e 1/2 xícaras (chá) de farinha de trigo;1 colher (sopa) de fermento em pó','Em um liquidificador, adicione a cenoura, os ovos e o óleo, depois misture.\n" +
                    "Acrescente o açúcar e bata novamente por 5 minutos.\n" +
                    "Em uma tigela ou na batedeira, adicione a farinha de trigo e depois misture novamente.\n" +
                    "Acrescente o fermento e misture lentamente com uma colher.\n" +
                    "Asse em um forno preaquecido a 180° C por aproximadamente 40 minutos.',NULL);")


    //singleton da classe
    companion object {
        private var instance: BancoDadosHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): BancoDadosHelper {
            if (instance == null) {
                instance = BancoDadosHelper(ctx.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(RECEITAS_DB_NAME, true,
            "id" to INTEGER + PRIMARY_KEY + UNIQUE ,
            "foto" to TEXT,
            "nome" to TEXT,
            "autor" to TEXT,
            "email" to TEXT,
            "descricao" to TEXT,
            "ingredientes" to TEXT,
            "modopreparo" to TEXT
        )

        // insere dados iniciais na tabela
        scriptSQLCreate.forEach {sql ->
            db.execSQL(sql)
        }
    }
    //só é chamado quando a versão do banco for atualizada
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        db.dropTable(RECEITAS_DB_NAME, true)
        onCreate(db)

    }
}
//instancia do singleton
val Context.database: BancoDadosHelper get() = BancoDadosHelper.getInstance(applicationContext)