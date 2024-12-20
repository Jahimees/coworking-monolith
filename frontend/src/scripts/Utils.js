var utils = {

    getUserFullName(user) {
        if (user != null && typeof user !== "undefined") {
            let fullName =
                (user.lastName != null ? user.lastName + " " : "") +
                (user.firstName != null ? user.firstName + " " : "") +
                (user.middleName != null ? user.middleName : "")

            if (fullName == null || fullName.trim() === "") {
                return user.username;
            } else {
                return fullName
            }
        }

        return ""
    },

    getCookie(name) {
        let matches = document.cookie.match(new RegExp(
            "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
        ));
        return matches ? decodeURIComponent(matches[1]) : '';
    },

    redirectToAuthIfNotAuthorized() {
        if (this.getCookie("_at") === '') {
            document.location.href = "/auth"
        }
    },

    logout() {
        setCookie("_at", "")
    },

    parseJwt(token) {
        console.log(token)
        var base64Url = token.split('.')[1];
        var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        var jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function (c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        return JSON.parse(jsonPayload);
    },

    isOverlapping(newStart, newEnd, events) {
        return events.some(event => {
            // Проверяем, пересекается ли диапазон
            return newStart < event.end && newEnd > event.start;
        });
    },


    async doInfoAction() {
        const location = window.location.href
        const paramsString = window.location.search
        const urlParams = new URLSearchParams(paramsString)
        const info = urlParams.get("i")

        if (info !== null) {
            switch (info) {
                case "reg": {
                    alert("Регистрация прошла успешно")
                    break
                }
                case "auth": {
                    alert("Авторизация прошла успешно")
                }
            }

            window.location.href = location.substring(0, location.indexOf(paramsString))
        }
    },

    UUID() {
        return "10000000-1000-4000-8000-100000000000".replace(/[018]/g, c =>
            (+c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> +c / 4).toString(16)
        );
    }
}

function setCookie(name, value, options = {}) {

    options = {
        path: '/',
        // при необходимости добавьте другие значения по умолчанию
        ...options
    };

    if (options.expires instanceof Date) {
        options.expires = options.expires.toUTCString();
    }

    let updatedCookie = encodeURIComponent(name) + "=" + encodeURIComponent(value);

    for (let optionKey in options) {
        updatedCookie += "; " + optionKey;
        let optionValue = options[optionKey];
        if (optionValue !== true) {
            updatedCookie += "=" + optionValue;
        }
    }

    document.cookie = updatedCookie;
}

export default utils
