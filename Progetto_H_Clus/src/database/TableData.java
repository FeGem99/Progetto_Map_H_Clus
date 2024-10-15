package database;

import data.Example;
import database.TableSchema.Column;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TableData {
    private DbAccess db;

    public TableData (DbAccess db){
       this.db =db;
    }

    public List<Example> getDistinctTransazioni(String table) throws SQLException,
EmptySetException,MissingNumberException, DatabaseConnectionException{
    List<Example> transactions = new ArrayList<>();

    TableSchema schema = new TableSchema(db, table);
    // verifico se ci sono attributi non numerici
    for (int i = 0; i<schema.getNumberOfAttributes(); i++){
        Column column = schema.getColumn(i);
        if (!column.isNumber()){
            throw new MissingNumberException("Attributo numerico trovato: " + column.getColumnName());
        }
    }

    Connection connection =db.getConnection();
    Statement statement = connection.createStatement();
    String query = "SELECT DISTINCT * FROM " + table;
    ResultSet resultSet = statement.executeQuery(query);
    if(! resultSet.next()){
        throw new EmptySetException ("La tabella " + table + " è vuota");
    }
    //torna al primo record
    resultSet.beforeFirst();

    while(resultSet.next()) {
        Example example=new Example();
        for(int i=0; i<schema.getNumberOfAttributes(); i++){
            double value =resultSet.getDouble(i+1);
            example.add(value);
        }
        transactions.add(example);
    }

    resultSet.close();
    statement.close();
    connection.close();

    
    return transactions;
    }
}

