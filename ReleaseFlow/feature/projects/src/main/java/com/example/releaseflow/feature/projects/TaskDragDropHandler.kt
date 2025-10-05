package com.example.releaseflow.feature.projects

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.zIndex
import com.example.releaseflow.core.domain.model.Task

class DragDropState {
    var draggedTask by mutableStateOf<Task?>(null)
    var dragOffset by mutableStateOf(Offset.Zero)
    var dropIndex by mutableStateOf<Int?>(null)
}

@Composable
fun rememberDragDropState() = remember { DragDropState() }

fun Modifier.draggableTask(
    task: Task,
    dragDropState: DragDropState,
    onDragStart: () -> Unit = {},
    onDragEnd: () -> Unit = {}
): Modifier = this.then(
    Modifier.pointerInput(task.id) {
        detectDragGesturesAfterLongPress(
            onDragStart = {
                dragDropState.draggedTask = task
                onDragStart()
            },
            onDrag = { change, dragAmount ->
                change.consume()
                dragDropState.dragOffset += dragAmount
            },
            onDragEnd = {
                onDragEnd()
                dragDropState.draggedTask = null
                dragDropState.dragOffset = Offset.Zero
            }
        )
    }
)
