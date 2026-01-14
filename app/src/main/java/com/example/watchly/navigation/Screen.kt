package com.example.watchly.navigation

sealed class Screen(val route:String) {

    object Home:Screen("home")
    object Detail:Screen("detail/{itemId}"){
        fun createRoute(itemId:Int)="detail/$itemId"
    }
}