package com.payments.app.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.payments.app.core.designsystem.preview.PaymentsPreview
import com.payments.app.core.designsystem.preview.PreviewThemes

/** A group of [LabeledValueRow]s, one per `label to value` pair, in order. */
@Composable
fun LabeledValueRows(
    rows: List<Pair<String, String>>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        rows.forEach { (label, value) -> LabeledValueRow(label = label, value = value) }
    }
}

@PreviewThemes
@Composable
private fun LabeledValueRowsPreview() {
    PaymentsPreview {
        LabeledValueRows(
            rows = listOf("Status:" to "Passed", "Card number:" to "****2345", "Terminal name:" to "EMV"),
            modifier = Modifier.padding(16.dp),
        )
    }
}
