import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*
import com.aula.constants.RECEITAS_DB_NAME

class BancoDadosHelper(context: Context) :
    ManagedSQLiteOpenHelper(ctx = context , name = "$RECEITAS_DB_NAME.db",  version = 1) {
    //ela executa quando o aplicativo rodas
//    private val scriptSQLCreate = arrayOf(
//        "INSERT INTO $RECEITAS_DB_NAME VALUES(1,'fernando.collor@gmail.com','Fernando Collor','R. Assungui, 27, Cursino, São Paulo, 04131-000, Brasil',800200300,NULL,'www.google.com.br',NULL);",
//        "INSERT INTO $RECEITAS_DB_NAME VALUES(2,'dilma@gmail.com','Dilma','R. José Cocciuffo, 90 - Cursino, São Paulo, 04121-120, Brasil',800235468,NULL,'www.uol.com.br',NULL);",
//        "INSERT INTO $RECEITAS_DB_NAME VALUES(3,'lula@gmail.com','Lula','R. José Cocciuffo, 56 - Cursino, São Paulo, 04121-120, Brasil',80023587,NULL,'www.google.com',NULL);",
//        "INSERT INTO $RECEITAS_DB_NAME VALUES(4,'maluf@gmail.com','Maluf','R. Camilo José, 48 - Cursino, São Paulo, 04125-140, Brasil',800025774,NULL,'www.uol.com.br',NULL);")
//    private val scriptSQLCreate = arrayOf(
//            "INSERT INTO $RECEITAS_DB_NAME (id, nome, autor, ingredientes, modopreparo, data, foto) VALUES (1,'Bolo de cenoura','Mateus','Cenoura','Mistura tudo',800200300,NULL);")


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
            "nome" to TEXT,
            "autor" to TEXT,
            "ingredientes" to TEXT,
            "modopreparo" to TEXT,
            "data" to INTEGER,
            "foto" to TEXT
        )

        // insere dados iniciais na tabela
//        scriptSQLCreate.forEach {sql ->
//            db.execSQL(sql)
//        }
    }
    //só é chamado quando a versão do banco for atualizada
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        db.dropTable(RECEITAS_DB_NAME, true)
        onCreate(db)

    }
}
//instancia do singleton
val Context.database: BancoDadosHelper get() = BancoDadosHelper.getInstance(applicationContext)