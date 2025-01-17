package com.example.meet_9.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.project7.ui.view.mahasiswa.DestinasiInsert
import com.example.project7.ui.view.mahasiswa.InsertMhsView

@Composable
fun PengelolaHalaman(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
){
    NavHost (navController = navController, startDestination = DestinasiHome.route){

        composable(
            route = DestinasiHome.route
        ){
            HomeMhsView(
                onDetailClick = { nim ->
                    navController.navigate("${DestinasiDetail.route}/$nim")
                    println(
                        "PengelolaHalaman: nim = $nim"
                    )
                },
                onAddMhs ={
                    navController.navigate("${DestinasiDetail.route}/$nim")
                },
                modifier = modifier
        }
    }

    composable(
        route = DestinasiInsert.route
    ){
        InsertMhsView(
            onBack = {
                navController.popBackStack()
            },
            onNavigate = {
                navController.popBackStack()
            },
            modifier = modifier,
        )
    }

    composable(
        DestinasiDetail.routesWithArg,
        arguments = listOf(
            navArgument(DestinasiDetail.NIM){
                type = NavType.StringType
            }
        )
    ) {
        val nim = it.arguments?.getString(DestinasiDetail.NIM)
        nim.let { nim ->
            DetailMhsView(
                onBack = {
                    navController.popBackStack()
                },
                onEditClick = {
                    navController.navigate("${DestinasiEdit.route}/$it")
                },
                modifier = modifier,
                onDeleteClick = {
                    navController.popBackStack()
                }
            )
        }
    }

    composable(
        DestinasiEdit.routesWithArg,
        arguments = listOf(
            navArgument(DestinasiEdit.NIM){
                type = NavType.StringType
            }
        )
    ){
        UpdateMhsView(
            onBack = {
                navController.popBackStack()
            },
            onNavigate = {
                navController.popBackStack()
            },
            modifier = modifier
        )
    }
}
