package com.example.pablo_room

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var userList by remember {
        mutableStateOf<List<User>>(emptyList())
    }

    var editMode by remember {
        mutableStateOf(false)
    }

    var textBt by remember {
        mutableStateOf("Cadastrar")
    }

    var idEdit by remember {
        mutableStateOf(0)
    }

    val context = LocalContext.current
    val dbConnection = AppDataBase.getDatabase(context)
    val userDAO = dbConnection.userDAO()

    LaunchedEffect(Unit) {
        try {
            userList = userDAO.listUser()
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Erro ao carregar usuários: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(text = "User List", fontSize = 25.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Nome") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "E-mail") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            if (name.isNotBlank() && email.isNotBlank()) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {

                        if (!editMode) {
                            userDAO.saveUser(User(0, name, email))
                        }else {

                            userDAO.attUser(User(idEdit,name,email))
                            editMode = false
                            idEdit = 0
                            textBt = "Cadastrar usuário"

                        }

                        delay(500)
                        userList = userDAO.listUser()
                        name = ""
                        email = ""

                    } catch (e: Exception) {
                        Log.e("Erro BD", "Erro ao conectar ao DB: ${e.message}")
                    }
                }
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = textBt)
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(Modifier.fillMaxWidth()) {

            items(userList) {

                    userAtual -> ItemLayout(user = userAtual)

                Column {

                    Text("Nome: ${userAtual.name}", fontSize = 20.sp)
                    Text("E-mail: ${userAtual.email}")
                }

                Row {

                    Button(onClick = {

                        name = userAtual.name
                        email = userAtual.email
                        idEdit = userAtual.id
                        editMode = true
                        textBt = "Edit User"



                    }) {
                        Text(text = "Edit")
                    }


                    Button(onClick = {

                        try {

                            CoroutineScope(Dispatchers.IO).launch {

                                userDAO.deleteUser(userAtual)
                                delay(500)
                                userList = userDAO.listUser()
                            }

                        }

                        catch (e: Exception){
                            Log.e("Erro DB", "${e.message}")
                        }

                    }) {
                        Text(text = "Delet")
                    }
                }
            }
        }
    }
}

@Composable
fun ItemLayout(user: User) {

}






