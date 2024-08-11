package com.baldomero.napoli.supabase.auth.utils

interface RouteProvider {
    fun getRouteForLoggedInUser(): String
    fun getRouteForLoggedOutUser(): String
}