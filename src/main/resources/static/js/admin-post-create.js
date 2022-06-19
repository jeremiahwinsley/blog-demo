document.addEventListener('DOMContentLoaded', function () {
  const simplemde = new SimpleMDE({
    indentWithTabs: false,
    autoDownloadFontAwesome: false,
    element: document.getElementById('body'),
    spellChecker: false
  });

  // document.getElementById('submit-form').addEventListener('submit', function (event) {
  //   const data = this;
  //   fetch(data.getAttribute('action'), {
  //     headers: {
  //       "Accept": "application/json",
  //       "Content-Type": "application/json"
  //     },
  //     method: data.getAttribute('method'),
  //     body: JSON.stringify(data)
  //   }).then(response => {
  //       if (response.status === 302) {
  //         window.location.href = response.headers.get('location');
  //       } else {
  //         response.json().then(value => value.)
  //       }
  //   }, err => console.error(err))
  //   event.preventDefault();
  // });
  //
  // const handleError = err => {
  //   console.error(err)
  //
  // }
});
