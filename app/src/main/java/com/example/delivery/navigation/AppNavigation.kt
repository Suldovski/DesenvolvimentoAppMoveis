package com.example.delivery.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import androidx.compose.runtime.LaunchedEffect
import com.example.delivery.auth.SessaoUsuario
import com.example.delivery.ui.screens.CadastroScreen
import com.example.delivery.ui.screens.CameraScreen
import com.example.delivery.ui.screens.CarrinhoScreen
import com.example.delivery.ui.screens.CarrinhoState
import com.example.delivery.ui.screens.CategoriaCadastroScreen
import com.example.delivery.ui.screens.CategoriaEditarScreen
import com.example.delivery.ui.screens.CategoriasScreen
import com.example.delivery.ui.screens.ConfirmadoScreen
import com.example.delivery.ui.screens.LoginScreen
import com.example.delivery.ui.screens.ProdutoCadastroScreen
import com.example.delivery.ui.screens.ProdutoEditarScreen
import com.example.delivery.ui.screens.ProdutoScreen
import com.example.delivery.ui.screens.ProdutosScreen
import com.example.delivery.ui.screens.RestauranteCadastroScreen
import com.example.delivery.ui.screens.RestauranteEditarScreen
import com.example.delivery.ui.screens.RestaurantesScreen
import com.example.delivery.ui.screens.RestauranteProdutosScreen
import com.example.delivery.ui.screens.UsuarioEditarScreen
import com.example.delivery.ui.screens.UsuariosScreen
import com.example.delivery.ui.viewmodel.CategoriaViewModel
import com.example.delivery.ui.viewmodel.ProdutoViewModel
import com.example.delivery.ui.viewmodel.RestauranteViewModel
import com.example.delivery.ui.viewmodel.UsuarioViewModel

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    val rotaAtual by navController.currentBackStackEntryAsState()
    val destino = rotaAtual?.destination
    val usuarioAtual = SessaoUsuario.usuarioId

    LaunchedEffect(destino, usuarioAtual) {
        val publica = destino?.hasRoute<Login>() == true ||
                destino?.hasRoute<Cadastro>() == true

        if (destino != null && !publica && usuarioAtual == null) {
            navController.navigate(Login) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Login,
        modifier = modifier
    ) {
        composable<Login> {
            LoginScreen(
                viewModel = viewModel(),
                loginSucesso = {
                    navController.navigate(Restaurantes) {
                        popUpTo(Login) { inclusive = true }
                    }
                },
                irCadastro = { navController.navigate(Cadastro) }
            )
        }

        composable<Cadastro> {
            CadastroScreen(
                viewModel = viewModel(),
                irLogin = { navController.popBackStack() }
            )
        }

        composable<Restaurantes> { entrada ->
            val restauranteViewModel: RestauranteViewModel = viewModel()
            DisposableEffect(entrada) {
                val observer = LifecycleEventObserver { _, evento ->
                    if (evento == Lifecycle.Event.ON_RESUME) restauranteViewModel.listar()
                }
                entrada.lifecycle.addObserver(observer)
                onDispose { entrada.lifecycle.removeObserver(observer) }
            }
            RestaurantesScreen(
                viewModel = restauranteViewModel,
                irParaProduto = { id, nome -> navController.navigate(RestauranteProdutos(id, nome)) },
                irParaCadastro = { navController.navigate(RestauranteCadastrar) },
                irParaEdicao = { navController.navigate(RestauranteEditar(it)) }
            )
        }

        composable<RestauranteProdutos> { entrada ->
            val rota = entrada.toRoute<RestauranteProdutos>()
            val produtoViewModel: ProdutoViewModel = viewModel()
            RestauranteProdutosScreen(
                viewModel = produtoViewModel,
                nomeRestaurante = rota.nome,
                aoAdicionar = { nome, preco ->
                    CarrinhoState.adicionar(nome, preco, 1)
                },
                voltar = { navController.navigate(Carrinho) }
            )
        }

        composable<RestauranteCadastrar> {
            RestauranteCadastroScreen(
                viewModel = viewModel(),
                voltar = { navController.popBackStack() }
            )
        }

        composable<RestauranteEditar> { entrada ->
            val rota = entrada.toRoute<RestauranteEditar>()
            RestauranteEditarScreen(
                restauranteId = rota.id,
                viewModel = viewModel(),
                voltar = { navController.popBackStack() }
            )
        }

        composable<Produto> { entrada ->
            val rota = entrada.toRoute<Produto>()
            val produtoViewModel: ProdutoViewModel = viewModel()

            LaunchedEffect(rota.id) {
                produtoViewModel.buscarPorId(rota.id)
            }

            produtoViewModel.produto?.let { produto ->
                ProdutoScreen(
                    produto = produto,
                    irParaCarrinho = { navController.navigate(Carrinho) },
                    aoAdicionar = { nome, preco, quantidade ->
                        CarrinhoState.adicionar(nome, preco, quantidade)
                    }
                )
            }
        }

        composable<Carrinho> {
            CarrinhoScreen(
                irParaConfirmacao = {
                    CarrinhoState.limpar()
                    navController.navigate(Confirmado)
                },
                adicionarMaisProdutos = { navController.navigate(Restaurantes) }
            )
        }

        composable<Confirmado> {
            ConfirmadoScreen(
                irParaRestaurantes = {
                    navController.popBackStack(route = Restaurantes, inclusive = false)
                }
            )
        }

        composable<Usuarios> { entrada ->
            val usuarioViewModel: UsuarioViewModel = viewModel()
            DisposableEffect(entrada) {
                val observer = LifecycleEventObserver { _, evento ->
                    if (evento == Lifecycle.Event.ON_RESUME) usuarioViewModel.listar()
                }
                entrada.lifecycle.addObserver(observer)
                onDispose { entrada.lifecycle.removeObserver(observer) }
            }
            UsuariosScreen(
                viewModel = usuarioViewModel,
                irParaCadastro = { navController.navigate(CadastrarUsuario) },
                irParaEdicao = { navController.navigate(UsuarioEditar(it)) },
                voltar = { navController.popBackStack() }
            )
        }

        composable<CadastrarUsuario> {
            CadastroScreen(
                viewModel = viewModel(),
                irLogin = { navController.popBackStack() }
            )
        }

        composable<UsuarioEditar> { entrada ->
            val rota = entrada.toRoute<UsuarioEditar>()
            UsuarioEditarScreen(
                usuarioId = rota.id,
                viewModel = viewModel(),
                voltar = { navController.popBackStack() }
            )
        }

        composable<Categorias> { entrada ->
            val categoriaViewModel: CategoriaViewModel = viewModel()
            DisposableEffect(entrada) {
                val observer = LifecycleEventObserver { _, evento ->
                    if (evento == Lifecycle.Event.ON_RESUME) categoriaViewModel.listar()
                }
                entrada.lifecycle.addObserver(observer)
                onDispose { entrada.lifecycle.removeObserver(observer) }
            }
            CategoriasScreen(
                viewModel = categoriaViewModel,
                irParaCadastro = { navController.navigate(CategoriaCadastrar) },
                irParaEdicao = { navController.navigate(CategoriaEditar(it)) }
            )
        }

        composable<CategoriaCadastrar> {
            CategoriaCadastroScreen(
                viewModel = viewModel(),
                voltar = { navController.popBackStack() }
            )
        }

        composable<CategoriaEditar> { entrada ->
            val rota = entrada.toRoute<CategoriaEditar>()
            CategoriaEditarScreen(
                categoriaId = rota.id,
                viewModel = viewModel(),
                voltar = { navController.popBackStack() }
            )
        }

        composable<Produtos> { entrada ->
            val produtoViewModel: ProdutoViewModel = viewModel()
            DisposableEffect(entrada) {
                val observer = LifecycleEventObserver { _, evento ->
                    if (evento == Lifecycle.Event.ON_RESUME) produtoViewModel.listar()
                }
                entrada.lifecycle.addObserver(observer)
                onDispose { entrada.lifecycle.removeObserver(observer) }
            }
            ProdutosScreen(
                viewModel = produtoViewModel,
                irParaCadastro = { navController.navigate(ProdutoCadastrar) },
                irParaEdicao = { navController.navigate(ProdutoEditar(it)) },
                irParaCompra = { navController.navigate(Produto(it)) }
            )
        }

        composable<ProdutoCadastrar> {
            ProdutoCadastroScreen(
                viewModel = viewModel(),
                voltar = { navController.popBackStack() }
            )
        }

        composable<ProdutoEditar> { entrada ->
            val rota = entrada.toRoute<ProdutoEditar>()
            ProdutoEditarScreen(
                produtoId = rota.id,
                viewModel = viewModel(),
                voltar = { navController.popBackStack() }
            )
        }

        composable<Camera> {
            CameraScreen()
        }
    }
}
