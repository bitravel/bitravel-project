//cleave form formats
var inputFormatter = function () {
    var input = document.querySelectorAll('[data-format]');
    if (input.length === 0) return;

    for (var i = 0; i < input.length; i++) {
        var inputFormat = input[i].dataset.format,
            blocks = input[i].dataset.blocks,
            delimiter = input[i].dataset.delimiter;
        blocks = blocks !== undefined ? blocks.split(' ').map(Number) : '';
        delimiter = delimiter !== undefined ? delimiter : ' ';

        switch (inputFormat) {
            case 'card':
                var card = new Cleave(input[i], {
                    creditCard: true
                });
                break;
                case 'phone':
                var phone = new Cleave(input[i], {
                    numericOnly: true,
                    blocks: [0, 3, 3, 4],
                    delimiters: ["(", ") ", "-"]
                });
                break;
            case 'cvc':
                var cvc = new Cleave(input[i], {
                    numeral: true,
                    numeralIntegerScale: 3
                });
                break;
            case 'date':
                var date = new Cleave(input[i], {
                    date: true,
                    datePattern: ['m', 'y']
                });
                break;

            case 'date-long':
                var dateLong = new Cleave(input[i], {
                    date: true,
                    datePattern: ['Y', 'm', 'd']
                });
                break;

            case 'time':
                var time = new Cleave(input[i], {
                    time: true,
                    timePattern: ['h', 'm']
                });
                break;
                case 'time-long':
                    var time = new Cleave(input[i], {
                        time: true,
                        timePattern: ['h', 'm', 's']
                    });
                    break;
            case 'custom':
                var custom = new Cleave(input[i], {
                    delimiter: delimiter,
                    blocks: blocks
                });
                break;

            default:
                console.error('Sorry, your format ' + inputFormat + ' is not available. You can add it to the theme object method - inputFormatter in src/js/theme.js or choose one from the list of available formats: card, cvc, date, date-long, time or custom.');
        }
    }
}();