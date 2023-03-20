#![cfg_attr(
    all(not(debug_assertions), target_os = "windows"),
    windows_subsystem = "windows"
)]

// Learn more about Tauri commands at https://tauri.app/v1/guides/features/command
#[tauri::command]
fn unlock(name: &str) -> String {
    format!("Typed password is: {}", name)
}

fn main() {
    tauri::Builder::default()
        .invoke_handler(tauri::generate_handler![unlock])
        .run(tauri::generate_context!())
        .expect("error while running tauri application");
}

