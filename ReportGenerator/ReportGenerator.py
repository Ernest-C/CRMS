from flask import Flask, request, jsonify
import mysql.connector
from fpdf import FPDF

app = Flask(__name__)

# Database configuration
db_config = {
    'user': 'root',
    'password': 'undeuxtrois123',
    'host': 'localhost',
    'database': 'carrentaldb',
}

@app.route('/generate_report', methods=['POST'])
def generate_report():
    try:
        # Get the report type and output file name from the request
        data = request.json
        report_type = data.get('report_type')
        output_file = data.get('output_file')

        # Validate inputs
        if not report_type or not output_file:
            return jsonify({"error": "Missing report_type or output_file"}), 400

        # Connect to the database and fetch data (same as before)
        conn = mysql.connector.connect(**db_config)
        cursor = conn.cursor()
        query = """
            SELECT rentals.rental_id, customers.first_name || ' ' || customers.last_name AS customer_name,
                   cars.make || ' ' || cars.model AS car_name, rentals.rental_date, rentals.return_date
            FROM rentals
            JOIN customers ON rentals.customer_id = customers.customer_id
            JOIN cars ON rentals.car_id = cars.car_id
        """
        cursor.execute(query)
        data = cursor.fetchall()
        conn.close()

        # Generate the report
        if report_type == "text":
            with open(output_file, 'w') as f:
                for row in data:
                    f.write(", ".join(map(str, row)) + "\n")
            return jsonify({"message": f"Text report generated: {output_file}"}), 200
        elif report_type == "pdf":
            pdf = FPDF()
            pdf.set_auto_page_break(auto=True, margin=15)
            pdf.add_page()
            pdf.set_font("Arial", size=12)

            for row in data:
                pdf.cell(0, 10, ", ".join(map(str, row)), ln=True)

            pdf.output(output_file)
            return jsonify({"message": f"PDF report generated: {output_file}"}), 200
        else:
            return jsonify({"error": "Invalid report type. Please choose 'text' or 'pdf'."}), 400

    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)
