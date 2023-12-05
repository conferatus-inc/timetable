import { ref } from 'vue'

export const alertEnabled = ref(false)
export const alertText = ref('')

export function showAlert(text) {
    alertEnabled.value = true
    alertText.value = text
    setTimeout(() => alertEnabled.value = false, 2000)
}
