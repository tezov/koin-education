import UIKit
import SwiftUI
import SharedKit


class FromIosProtocolImpl: FromIosProtocol {
    func getString() -> String {
        "Hello from iOS!"
    }
    
    func getInt() -> Int32 {
        42
    }
}


struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(
            fromIosProtocol: { FromIosProtocolImpl() }
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea()
    }
}



