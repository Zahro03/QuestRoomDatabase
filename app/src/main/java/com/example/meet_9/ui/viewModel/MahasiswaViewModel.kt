package com.example.meet_9.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meet_9.data.entity.Mahasiswa
import com.example.meet_9.repository.RepositoryMhs
import kotlinx.coroutines.launch

class MahasiswaViewModel (private val repositoryMhs: RepositoryMhs) : ViewModel() {
    var uiState by mutableStateOf(MhsUIState())

    // memperbarui state berdasarkan input pengguna
    fun updateState(mahasiswaEvent: MahasiswaEvent) {
        uiState = uiState.copy(
            mahasiswaEvent = mahasiswaEvent
        )
    }

    // validasi data input pengguna
    private fun validateFields(): Boolean {
        val event = uiState.mahasiswaEvent
        val errorState = FormErrorState(
            nim = if (event.nim.isNotEmpty()) null else "NIM tidak boleh kosong",
            nama = if (event.nim.isNotEmpty()) null else "Nama tidak boleh kosong",
            jenisKelamin = if (event.nim.isNotEmpty()) null else "Jenis Kelamin tidak boleh kosong",
            alamat = if (event.nim.isNotEmpty()) null else "Alamat tidak boleh kosong",
            kelas = if (event.nim.isNotEmpty()) null else "Kelas tidak boleh kosong",
            angkatan = if (event.nim.isNotEmpty()) null else "Angkatan tidak boleh kosong"
        )
        uiState = uiState.copy(isEntryValid = errorState)
        return errorState.isValid()
    }

        //Menyimpan data ke repository
        fun saveData() {
            val currentEvent = uiState.mahasiswaEvent

            if (validateFields()) {
                viewModelScope.launch {
                    try {
                        repositoryMhs.insertMhs(currentEvent.toMahaiswaEntity())
                        uiState = uiState.copy(
                            snackBarMessage = "Data berhasil disimpan",
                            mahasiswaEvent = MahasiswaEvent(), // Reset input Form
                            isEntryValid = FormErrorState() // Reset error state
                        )
                    } catch (e: Exception) {
                        uiState = uiState.copy(
                            snackBarMessage = "Data Gagal Disimpan"
                        )
                    }
                }
            } else {
                uiState = uiState.copy(
                    snackBarMessage = "Input tidak valid. Periksa kembali data anda."
                )
            }
        }

        fun resetSnackBarMessage(){
            uiState = uiState.copy(snackBarMessage = null)
        }
    }

//menyimpan input form ke daalam entity
    fun MahasiswaEvent.toMahaiswaEntity(): Mahasiswa = Mahasiswa(
        nim = nim,
        nama = nama,
        jenisKelamin = jenisKelamin,
        alamat = alamat,
        kelas = kelas,
        angkatan = angkatan
    )

    data class MahasiswaEvent(
        val nim: String = "",
        val nama: String = "",
        val jenisKelamin: String = "",
        val alamat: String = "",
        val kelas: String = "",
        val angkatan: String = "",
    )

// Bedanya event dan state
// Event adalah aksi yang merubah kondisi
// dan state merupakan  keadaan yang terjadi setelah ada trigger dari event
    data class MhsUIState(
        val mahasiswaEvent: MahasiswaEvent = MahasiswaEvent(),
        val isEntryValid: FormErrorState = FormErrorState(),
        val snackBarMessage: String? = null
    )

    data class FormErrorState(
        val nim: String? = null,
        val nama: String? = null,
        val jenisKelamin: String? = null,
        val alamat: String? = null,
        val kelas: String? = null,
        val angkatan: String? = null,
    ) {
        fun isValid(): Boolean {
            return nim == null && nama == null && jenisKelamin == null &&
                    alamat == null && kelas == null && angkatan == null
        }
    }

