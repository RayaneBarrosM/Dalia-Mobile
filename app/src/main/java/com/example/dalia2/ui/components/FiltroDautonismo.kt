package com.example.dalia2.ui.components

import android.text.BoringLayout
import androidx.appcompat.widget.VectorEnabledTintResources
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.ElementoListrado(
    enable: Boolean,
    tamanho: Float,
    color: Color
): Modifier =this.drawBehind {
    // Desenha a cor de fundo
    drawRect(color = color)
    if(enable){

    val stripeWidth = tamanho// Largura da listra
    val stripeSpace = 20f // Espaço entre as listras
    val strokeWidth = 8f
    val colorListra = Color(0xFFB71C1C).copy(alpha = 0.35f)

    var x = 0f
    while (x < size.width + size.height) {
        drawLine(
            color = colorListra,
            start = Offset(x, 0f),
            end = Offset(x - size.height, size.height),
            strokeWidth = strokeWidth
        )
        x += stripeWidth + stripeSpace
    }
    }
}

@Composable
fun Modifier.ElementoBolinhas(
    enable: Boolean,
    color: Color
): Modifier =this.drawBehind {
    // Desenha a cor de fundo
    drawRect(color = color)
    if(enable){
        val raioBolinha = 6f
        val espacamento = 40f
        val colorBolinha = Color(0xFF0D47A1).copy(alpha = 0.30f)
        // Loop para preencher a largura e altura com círculos
        var x = espacamento / 2
        while (x < size.width) {
            var y = espacamento / 2
            while (y < size.height) {
                drawCircle(
                    color = colorBolinha,
                    radius = raioBolinha,
                    center = Offset(x, y)
                )
                y += espacamento
            }
            x += espacamento
        }
    }
}

@Composable
fun Modifier.ElementoCruzado(
    enable: Boolean,
    color: Color,
): Modifier = this.drawBehind {
    drawRect(color = color)

    if (enable) {
        val corListra: Color = Color(0xFF01579B).copy(alpha = 0.35f)
        val stripeSpace: Float = 12f
        val strokeWidth: Float = 2f
        var x1 = -size.height
        while (x1 < size.width + size.height) {
            drawLine(
                color = corListra,
                start = Offset(x1, 0f),
                end = Offset(x1 + size.height, size.height),
                strokeWidth = strokeWidth
            )
            x1 += stripeSpace
        }
        var x2 = 0f
        while (x2 < size.width + size.height) {
            drawLine(
                color = corListra,
                start = Offset(x2, 0f),
                end = Offset(x2 - size.height, size.height),
                strokeWidth = strokeWidth
            )
            x2 += stripeSpace
        }
    }
}