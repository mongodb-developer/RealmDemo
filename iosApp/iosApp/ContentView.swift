import SwiftUI
import shared

struct ContentView: View {
    let greet = Greeting().greeting()
    
    @State var name:String = ""
    @State var twitterHandle: String = ""
    @State var userList = [User]()
    
    var realmRepo = Repo()
    
    
    var body: some View {
        VStack {
            
            Spacer()
                .frame(height: 50)
            
            TextField("Your Name", text: $name)
                .textFieldStyle(.roundedBorder)
                .padding(EdgeInsets(top: 0, leading: 20, bottom: 0, trailing: 20))
            
            TextField("Your Handle",text: $twitterHandle)
                .textFieldStyle(.roundedBorder)
                .padding(EdgeInsets(top: 10, leading: 20, bottom: 10, trailing: 20))
            
            Button("Save") {
                
                let info = User()
                info.name = name
                info.twitterHandle = twitterHandle
                
                realmRepo.saveUserInfo(user: info) { _ in
                }
            }
            .padding(10)
            .overlay(
                RoundedRectangle(cornerRadius: 8)
                    .stroke(Color.blue, lineWidth: 1)
            )
            
            
            Divider().padding(20)
            
            Text("User List")
            
            List(userList, id: \.self.name) { info in
                HStack {
                    VStack(alignment: .leading) {
                        Text(info.name)
                        Text(info.twitterHandle ?? "")
                    }
                    
                    Spacer()
                }
            }
        }.onAppear{
            register()
            getUserList()
        }
        .navigationBarTitle("SharingDataLayer",displayMode: .inline)
    }
    
    
    
    func register(){
        realmRepo.doAppSignIn { _ in
            print("Unexpected in anonymous sign in")
        }
    }
    
    func getUserList(){
        
        Task {
            do {
                try await realmRepo.getUsersAsFlow().watch(block: { items in
                    userList = items as! [User]
                })
                
            } catch {
                print("Unexpected: \(error)")
            }
        }
    }
    
    
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
