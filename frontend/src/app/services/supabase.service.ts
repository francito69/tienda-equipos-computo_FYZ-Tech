import { Injectable } from '@angular/core';
import { createClient, SupabaseClient } from '@supabase/supabase-js';
import { environment } from '../../environment';

export interface SupabaseConfig {
    url: string;
    anonKey: string;
}

@Injectable({
    providedIn: 'root'
})
export class SupabaseService {
    private supabase: SupabaseClient;

    constructor() {
        // Initialize Supabase client
        const config: SupabaseConfig = {
            url: environment.supabaseUrl,
            anonKey: environment.supabaseAnonKey
        };

        this.supabase = createClient(config.url, config.anonKey);
    }

    /**
     * Upload an image file to Supabase Storage
     * @param file - The file to upload
     * @param bucket - The storage bucket name (default: 'imgs')
     * @returns Promise with the public URL of the uploaded image
     */
    async uploadImage(file: File, bucket: string = 'imgs'): Promise<string> {
        const fileName = `${Date.now()}-${file.name}`;

        const { data, error } = await this.supabase.storage
            .from(bucket)
            .upload(fileName, file);

        if (error) {
            console.error('Error uploading image:', error);
            throw error;
        }

        const { data: urlData } = this.supabase.storage
            .from(bucket)
            .getPublicUrl(fileName);

        return urlData.publicUrl;
    }
}
