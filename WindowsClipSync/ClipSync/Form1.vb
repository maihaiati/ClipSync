Imports System.Runtime.InteropServices
Imports System.Windows.Forms
Imports System.Windows.Forms.VisualStyles.VisualStyleElement
Public Class Form1
    ' Windows API
    Private Const WM_CLIPBOARDUPDATE As Integer = &H31D
    Private Shared ReadOnly HWND_MESSAGE As IntPtr = New IntPtr(-3)

    ' Import các hàm API
    <DllImport("user32.dll", SetLastError:=True)>
    Private Shared Function AddClipboardFormatListener(hWnd As IntPtr) As Boolean
    End Function
    <DllImport("user32.dll", SetLastError:=True)>
    Private Shared Function RemoveClipboardFormatListener(hWnd As IntPtr) As Boolean
    End Function
    Protected Overrides Sub WndProc(ByRef m As Message)
        MyBase.WndProc(m)
        If m.Msg = WM_CLIPBOARDUPDATE Then
            ' Clipboard đã thay đổi, xử lý nội dung mới
            HandleClipboardChange()
        End If
    End Sub
    Private Sub HandleClipboardChange()
        If Clipboard.ContainsText() Then
            Dim clipboardText As String = Clipboard.GetText()
            TextBox1.Text = clipboardText  ' Hiển thị nội dung clipboard lên TextBox
        ElseIf Clipboard.ContainsImage() Then
            PictureBox1.Image = Clipboard.GetImage()  ' Hiển thị ảnh từ clipboard
        End If
    End Sub

    Private Sub Form1_Load(sender As Object, e As EventArgs) Handles MyBase.Load
        ' Bắt đầu lắng nghe sự kiện clipboard
        AddClipboardFormatListener(Me.Handle)
    End Sub

    Private Sub Form1_FormClosing(sender As Object, e As FormClosingEventArgs) Handles MyBase.FormClosing
        ' Ngừng lắng nghe clipboard khi đóng form
        RemoveClipboardFormatListener(Me.Handle)
    End Sub
End Class