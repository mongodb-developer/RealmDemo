import SwiftUI
import shared

struct ContentView: View {
    @ObservedObject var vm = IOSViewModel()
    var body: some View {
        VStack {
            
            Spacer()
                .frame(height: 50)
            
            TextField("Your Name", text: $vm.name)
                .textFieldStyle(.roundedBorder)
                .padding(EdgeInsets(top: 0, leading: 20, bottom: 0, trailing: 20))
                .autocorrectionDisabled(false)

            TextField("Your Handle",text: $vm.twitterHandle)
                .textFieldStyle(.roundedBorder)
                .padding(EdgeInsets(top: 10, leading: 20, bottom: 10, trailing: 20))
                .autocorrectionDisabled(false)

            Button("Save") {
                let info = User()
                info.name = vm.name
                info.twitterHandle = vm.twitterHandle
                vm.saveUser(user: info)
                vm.twitterHandle = ""
                vm.name = ""
            }
            .padding(10)
            .overlay(
                RoundedRectangle(cornerRadius: 8)
                    .stroke(Color.blue, lineWidth: 1)
            )
            
            
            Divider().padding(20)
            
            Text("User List")
            
            List(vm.userList, id: \.self.name) { info in
                HStack {
                    VStack(alignment: .leading) {
                        Text(info.name)
                        Text(info.twitterHandle ?? "")
                    }
                    
                    Spacer()
                }
            }
        }
        .onAppear {
            vm.register()
        }
        .navigationBarTitle("SharingDataLayer",displayMode: .inline)
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
