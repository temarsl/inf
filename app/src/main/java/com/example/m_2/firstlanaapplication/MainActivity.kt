package com.example.m_2.firstlanaapplication
// Compose
// Coroutines
// Jetpack ViewModel: Для управления состоянием UI
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.m_2.R
import com.example.m_2.firstlanaapplication.ui.theme.M2Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

var isLoggedIn = false

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            M2Theme { // задает тему для всего интерфейса.
                Surface( // используется для задания фона и других визуальных параметров.
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HorizontalPagerWithFunctions() // определяет пользовательский интерфейс.
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
//  Composable функция, которая создает горизонтальный пейджер.
fun HorizontalPagerWithFunctions() {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })
    Column {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top
        ) { page ->
            // какая страница должна быть отображена
            when (page) {
                0 -> Log(scope, pagerState)
                1 -> Reg(scope, pagerState)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Log(
    scope: CoroutineScope,
    pagerState: PagerState,
    mainViewModel: MainViewModel = viewModel(factory = MainViewModel.factory)
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val maxLength = 18
    var text by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp, start = 25.dp, end = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Лого",
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 50.dp)
        )

        Row(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CustomButton(
                text = "Вход",
                isSelected = pagerState.currentPage == 0,
                onClick = { scope.launch { pagerState.animateScrollToPage(0) } }
            )
            CustomButton(
                text = "Регистрация",
                isSelected = pagerState.currentPage == 1,
                onClick = { scope.launch { pagerState.animateScrollToPage(1) } }
            )
        }

        TextField(
            value = login,
            onValueChange = { newlogin ->
                if (newlogin.length <= maxLength) {
                    login = newlogin.filter { !it.isWhitespace() }
                }
            },
            label = { Text("Логин") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 13.dp, vertical = 5.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true
        )
        TextField(
            value = password,
            onValueChange = { newpassword ->
                if (newpassword.length <= maxLength) {
                    password = newpassword.filter { !it.isWhitespace() }
                }
            },
            label = { Text("Пароль") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 13.dp, vertical = 5.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = {
                if (mainViewModel.login_search(login)) {
                    if (login.isBlank() && password.isBlank()) {
                        text = "Ошибка! Логин и пароль должны быть заполнены."
                        return@Button
                    }
                    if (mainViewModel.login(login, password)) {
                        text = "Вход выполнен"
                        isLoggedIn = true
                    } else {
                        text = "Ошибка! Неверный пароль"
                    }
                } else {
                    text = "Ошибка! Пользователь с введеным логином не найден"
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Войти")
        }

        Text(
            text = text,
            color = Color.Red,
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Reg(
    scope: CoroutineScope,
    pagerState: PagerState,
    mainViewModel: MainViewModel = viewModel(factory = MainViewModel.factory)
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }
    val maxLength = 18
    var text by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp, start = 15.dp, end = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Лого",
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 50.dp)
        )

        Row(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CustomButton(
                text = "Вход",
                isSelected = pagerState.currentPage == 0,
                onClick = { scope.launch { pagerState.animateScrollToPage(0) } }
            )
            CustomButton(
                text = "Регистрация",
                isSelected = pagerState.currentPage == 1,
                onClick = { scope.launch { pagerState.animateScrollToPage(1) } }
            )
        }

        TextField(
            value = login,
            onValueChange = { newLogin ->
                if (newLogin.length <= maxLength) {
                    login = newLogin.filter { !it.isWhitespace() }
                }
            },
            label = { Text("Логин") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true
        )
        TextField(
            value = password,
            onValueChange = { newPassword ->
                if (newPassword.length <= maxLength) {
                    password = newPassword.filter { !it.isWhitespace() }
                }
            },
            label = { Text("Пароль") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )
        TextField(
            value = password2,
            onValueChange = { newPassword2 ->
                if (newPassword2.length <= maxLength) {
                    password2 = newPassword2.filter { !it.isWhitespace() }
                }
            },
            label = { Text("Повторите пароль") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = {
                if (login.isBlank() && password.isBlank()) {
                    text = "Ошибка! Логин и пароль должны быть заполнены."
                    return@Button
                }
                if (mainViewModel.register(login, password)) {
                    text = "Регистрация выполнена"
                    isLoggedIn = true
                } else {
                    text = "Ошибка, пользователь уже есть в системе"
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Зарегистрироваться")
        }

        Text(
            text = text,
            color = Color.Red,
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}

@Composable
fun CustomButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFFA45066) else Color(0xFFA45066),
            contentColor = if (isSelected) Color.White else Color.Black
        ),
        modifier = Modifier
            .width(150.dp)
            .height(40.dp)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )
    }
}
