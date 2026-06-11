package com.phoenixforge.student.ui.digitalhome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phoenixforge.student.ui.components.StudentBackHeader
import com.phoenixforge.student.ui.components.StudentHearthBackground
import com.phoenixforge.student.ui.theme.StudentKidCopy
import com.phoenixforge.student.ui.theme.StudentRoomVisuals

@Composable
fun DigitalHomeRoomScreen(
    roomId: String,
    onBack: () -> Unit,
) {
    val (title, body) = StudentKidCopy.roomStory(roomId)

    StudentHearthBackground {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item { StudentBackHeader(onBack = onBack) }
            item {
                Text(StudentRoomVisuals.emoji(roomId), fontSize = 56.sp)
                Text(title, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            }
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                ) {
                    Text(
                        body,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(20.dp),
                    )
                }
            }
        }
    }
}
