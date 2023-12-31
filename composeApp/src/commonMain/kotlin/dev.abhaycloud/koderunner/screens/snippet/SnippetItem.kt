package dev.abhaycloud.koderunner.screens.snippet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.abhaycloud.koderunner.model.snippet.SnippetsModelItem
import dev.abhaycloud.koderunner.utils.Utils.convertDateToHumanReadableFormat
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SnippetItem(snippetItem: SnippetsModelItem, isPlatformDesktop: Boolean, modifier: Modifier, onClick: (SnippetsModelItem) -> Unit) {
    Box(
        modifier = modifier
            .padding(top = if(isPlatformDesktop) 0.dp else 16.dp)
            .padding(horizontal = if(isPlatformDesktop) 0.dp else 16.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .border(
                border = BorderStroke(1.dp, color = Color(0xffD8E0E9)),
                shape = RoundedCornerShape(10.dp)
            ).clickable {
                onClick.invoke(snippetItem)
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding( if(isPlatformDesktop) 24.dp else 16.dp)
        ) {
            Image(
                painterResource("languages/kotlin.png"),
                modifier = Modifier.size( if(isPlatformDesktop) 72.dp else 33.dp),
                contentDescription = "language"
            )
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(
                    snippetItem.title,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.W500
                    ),)
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(top = 14.dp)) {
                        Text(
                            snippetItem.modified.convertDateToHumanReadableFormat(),
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color(0xff7D7D7D),
                                fontWeight = FontWeight.W400
                            ),)
                        Text(
                            snippetItem.owner,
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color(0xff6E49C1),
                                fontWeight = FontWeight.W400,
                                textDecoration = TextDecoration.Underline
                            ),)
                    }
            }
        }
    }
}
