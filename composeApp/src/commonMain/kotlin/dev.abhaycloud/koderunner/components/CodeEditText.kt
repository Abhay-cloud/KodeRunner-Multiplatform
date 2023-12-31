package dev.abhaycloud.koderunner.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.BoldHighlight
import dev.snipme.highlights.model.ColorHighlight

private const val TAB_LENGTH = 4
private const val TAB_CHAR = "\t"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeEditText(
    highlights: Highlights,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    translateTabToSpaces: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = TextFieldDefaults.shape,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors()
) {
    val currentText = remember {
        mutableStateOf(
            TextFieldValue()
        )
    }

    TextField(
        modifier = modifier.fillMaxWidth(),
        onValueChange = {
            val fieldUpdate = if (translateTabToSpaces && it.text.contains(TAB_CHAR)) {
                val result = it.text.replace(TAB_CHAR, " ".repeat(TAB_LENGTH))
                it.copy(text = result, TextRange(it.selection.start + TAB_LENGTH - 1))
            } else {
                it
            }

            currentText.value = fieldUpdate
            onValueChange(fieldUpdate.text)
        },
        value = TextFieldValue(
            selection = currentText.value.selection,
            composition = currentText.value.composition,
            annotatedString = buildAnnotatedString {
                append(highlights.getCode())

                highlights.getHighlights()
                    .filterIsInstance<ColorHighlight>()
                    .forEach {
                        addStyle(
                            SpanStyle(color = Color(it.rgb).copy(alpha = 1f)),
                            start = it.location.start,
                            end = it.location.end,
                        )
                    }

                highlights.getHighlights()
                    .filterIsInstance<BoldHighlight>()
                    .forEach {
                        addStyle(
                            SpanStyle(fontWeight = FontWeight.Bold),
                            start = it.location.start,
                            end = it.location.end,
                        )
                    }
            },
        ),
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors,
    )
}