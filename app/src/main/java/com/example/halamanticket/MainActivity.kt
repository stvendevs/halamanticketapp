package com.example.halamanticket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    TicketScreen()
                }
            }
        }
    }
}


// ======================================================
// PARENT COMPOSABLE
// Semua state dikelola di sini
// ======================================================

@Composable
fun TicketScreen() {

    // STATE DIKELOLA OLEH PARENT
    var hargaTiket by rememberSaveable {
        mutableStateOf(50000)
    }

    var jumlahTiket by rememberSaveable {
        mutableStateOf(1)
    }

    var namaPembeli by rememberSaveable {
        mutableStateOf("")
    }

    var status by rememberSaveable {
        mutableStateOf("Silakan pesan tiket")
    }


    // ==================================================
    // LAUNCHED EFFECT
    // Berjalan ketika status berubah menjadi "Memproses..."
    // ==================================================

    LaunchedEffect(status) {

        if (status == "Memproses pesanan...") {

            // Simulasi proses pemesanan selama 5 detik
            delay(5000)

            status = "Tiket telah dipesan"
        }
    }


    // ==================================================
    // CHILD COMPOSABLE
    // State dikirim dari Parent ke Child
    // Event dikirim kembali ke Parent
    // ==================================================

    TicketContent(
        hargaTiket = hargaTiket,
        jumlahTiket = jumlahTiket,
        namaPembeli = namaPembeli,
        status = status,

        onNamaChange = {
            namaPembeli = it

            // Jika user mulai mengetik,
            // status dikembalikan ke status awal
            if (it.isNotEmpty() &&
                status == "Nama masih kosong"
            ) {
                status = "Silakan pesan tiket"
            }
        },

        onTambahTiket = {
            jumlahTiket++
        },

        onKurangTiket = {
            if (jumlahTiket > 1) {
                jumlahTiket--
            }
        },

        onPesanTiket = {

            // VALIDASI NAMA
            if (namaPembeli.isBlank()) {

                status = "Nama masih kosong"

            } else {

                // Mulai proses pemesanan
                status = "Memproses pesanan..."
            }
        }
    )
}


// ======================================================
// CHILD COMPOSABLE
// Stateless / tidak memiliki state sendiri
// ======================================================

@Composable
fun TicketContent(
    hargaTiket: Int,
    jumlahTiket: Int
    namaPembeli: String,
    status: String,

    onNamaChange: (String) -> Unit,
    onTambahTiket: () -> Unit,
    onKurangTiket: () -> Unit,
    onPesanTiket: () -> Unit
) {

    // Format harga menjadi Rupiah
    val formatRupiah = NumberFormat.getCurrencyInstance(
        Locale("id", "ID")
    )

    val totalHarga = hargaTiket * jumlahTiket

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {

        // ==================================================
        // JUDUL
        // ==================================================

        Text(
            text = "Pemesanan Tiket",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1769E0)
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )


        // ==================================================
        // NAMA PEMBELI
        // ==================================================

        Text(
            text = "Nama Pembeli",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        OutlinedTextField(
            value = namaPembeli,
            onValueChange = onNamaChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Masukkan nama Anda")
            },
            singleLine = true
        )


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        // ==================================================
        // HARGA TIKET
        // ==================================================

        Text(
            text = "Harga Tiket",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = formatRupiah.format(hargaTiket),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1769E0)
        )


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        // ==================================================
        // JUMLAH TIKET
        // ==================================================

        Text(
            text = "Jumlah Tiket",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            // BUTTON -
            Button(
                onClick = onKurangTiket,
                modifier = Modifier
                    .width(80.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = jumlahTiket > 1
            ) {
                Text(
                    text = "-",
                    fontSize = 22.sp
                )
            }


            Spacer(
                modifier = Modifier.width(30.dp)
            )


            // JUMLAH
            Text(
                text = jumlahTiket.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )


            Spacer(
                modifier = Modifier.width(30.dp)
            )


            // BUTTON +
            Button(
                onClick = onTambahTiket,
                modifier = Modifier
                    .width(80.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "+",
                    fontSize = 22.sp
                )
            }
        }


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        // ==================================================
        // TOTAL HARGA
        // ==================================================

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "Total",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = formatRupiah.format(totalHarga),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }


        Spacer(
            modifier = Modifier.height(24.dp)
        )


        // ==================================================
        // BUTTON PESAN
        // ==================================================

        Button(
            onClick = onPesanTiket,

            // Tidak bisa diklik ketika sedang memproses
            enabled = status != "Memproses pesanan...",

            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),

            shape = RoundedCornerShape(10.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1769E0)
            )
        ) {

            Text(
                text = "Pesan Tiket",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        // ==================================================
        // STATUS
        // ==================================================

        StatusBox(
            status = status
        )
    }
}


// ======================================================
// STATUS BOX
// ======================================================

@Composable
fun StatusBox(
    status: String
) {

    val statusColor = when {

        status == "Nama masih kosong" ->
            Color(0xFFD32F2F)

        status == "Memproses pesanan..." ->
            Color(0xFF1565C0)

        status == "Tiket telah dipesan" ->
            Color(0xFF2E7D32)

        else ->
            Color(0xFF37474F)
    }


    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = statusColor.copy(alpha = 0.10f)
    ) {

        Text(
            text = "Status: $status",
            modifier = Modifier.padding(16.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = statusColor
        )
    }
}