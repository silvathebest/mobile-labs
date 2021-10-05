function export2txt() {
  const originalData = {
    fruits: [
      {
        name: 'Apple',
        price: 35,
        checked: true
      },
      {
        name: 'Banana',
        price: 12,
        checked: false
      },
      {
        name: 'Grapes',
        price: 45,
        checked: false
      },
      {
        name: 'Pineapple',
        price: 200,
        checked: true
      }
    ]
  }

  const a = document.createElement('a')
  a.href = URL.createObjectURL(new Blob([JSON.stringify(originalData, null, 2)], {
    type: 'application/json'
  }))
  a.setAttribute('download', 'fruitsData.json')
  document.body.appendChild(a)
  a.click()
  document.body.removeChild(a)
}